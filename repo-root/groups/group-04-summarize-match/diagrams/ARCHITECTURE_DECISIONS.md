# Choix d'architecture

Ce document explique comment les diagrammes PlantUML de ce dossier correspondent au code de `summarize-match`, et pourquoi l'architecture respecte le DDD, l'architecture hexagonale et les principes SOLID.

## Index des diagrammes

- [domain-class-diagram.puml](domain-class-diagram.puml) documente la couche domaine : value objects, entites, events domaine, read models et `MatchAggregate`.
- [application-class-diagram.puml](application-class-diagram.puml) documente la couche application : cas d'usage, ports et orchestration applicative.
- [infrastructure-class-diagram.puml](infrastructure-class-diagram.puml) documente les adapters : consumer HTTP, validation de schema, mappers d'events, repositories, persistence et adapters techniques.
- [event-and-stats-sequence.puml](event-and-stats-sequence.puml) documente le flux principal a l'execution : `POST /events`, validation domaine, persistence, mise a jour des stats via fonction stockee et requetes de lecture.

## Choix DDD

### Bounded Context

`summarize-match` est traite comme un bounded context autonome. Sa responsabilite est de consommer des events de match, reconstruire la timeline, valider l'etat metier du match, puis exposer un resume et des statistiques.

Le catalogue d'events est volontairement place hors du crate Rust :

- `events/inbound/`
- `events/outbound/`

Le dossier `events/outbound/` est vide aujourd'hui de maniere intentionnelle : le bounded context ne publie pas encore d'event metier sortant.

### Racine d'agregat

Le diagramme de classe domaine est centre sur `MatchAggregate`.

`MatchAggregate` est la racine d'agregat parce que toutes les regles de coherence du match passent par lui :

- un match doit etre demarre avant d'accepter des events de jeu actifs ;
- `MATCH_STARTED` ne peut arriver qu'une seule fois ;
- les events terminaux bloquent les events actifs suivants ;
- `MATCH_FINISHED.finalScore` doit correspondre au score calcule ;
- une annulation de but met a jour le score calcule ;
- un joueur expulse ne peut plus produire d'actions de jeu ;
- les transitions pause/reprise sont controlees.

Ce choix garde les regles metier dans le domaine, au lieu de les disperser dans les handlers HTTP, les requetes SQL ou les adapters de persistence.

### Events domaine

Les events de transport entrants sont transformes en variantes typees de `DomainEvent`. Le domaine ne manipule pas du JSON brut.

Exemples :

- `MATCH_STARTED` -> `DomainEvent::MatchStarted`
- `GOAL_SCORED` -> `DomainEvent::GoalScored`
- `MATCH_FINISHED` -> `DomainEvent::MatchFinished`
- `PASS_ATTEMPTED` -> `DomainEvent::PassAttempted`

La couche de mapping joue donc le role de frontiere anti-corruption entre le contrat JSON entrant et le modele domaine.

### Value Objects

Le code utilise des value objects pour les identifiants et primitives metier :

- `MatchId`
- `EventId`
- `TeamId`
- `PlayerId`
- `MatchTime`
- `Score`

Ils evitent la primitive obsession. Par exemple, les UUID sont valides a la deserialisation ou a la construction, et `MatchTime` garantit des secondes et periodes valides.

### Read Models

Les read models sont separes de l'etat interne de l'agregat :

- `MatchSummary`
- `TeamStats`
- `PlayerStats`

L'agregat peut produire ces read models lors d'un replay en memoire, tandis que l'adapter PostgreSQL peut lire des statistiques materialisees via des fonctions stockees.

## Choix d'architecture hexagonale

### Le coeur applicatif depend de ports

Un port est une interface (un `trait` Rust) que la couche application definit elle-meme. Elle exprime ce dont elle a besoin sans savoir comment c'est fourni. L'infrastructure implemente ensuite ces traits : c'est elle qui "branche" sur les ports.

Ce choix suit le principe de l'architecture hexagonale (Ports & Adapters) : la politique metier et les cas d'usage ne dependent d'aucun framework, base de donnees ou protocole reseau. Les details techniques restent en dehors du hexagone.

Consequence directe : on peut remplacer PostgreSQL par SQLite, Axum par gRPC, ou brancher un faux repository en tests, sans toucher une seule ligne de code applicatif.

La couche application depend de traits, pas de classes d'infrastructure concretes.

#### Ports principaux et leur role

**`ApplicationService`** (`application.rs`)

Point d'entree unique pour les cas d'usage de commande (ecriture). Le handler HTTP recoit un JSON, le mappe en `DomainEvent`, puis appelle `handle_event`. Le service ne sait pas que l'appelant est un handler Axum.

```
async fn handle_event(&self, event: DomainEvent) -> ApplicationResult<()>
```

**`MatchRepository`** (`application/repository.rs`)

Port d'ecriture pour l'event store. Il offre deux operations :

- `load(match_id)` : recharge tous les events passes du match et rejoue l'agregat en memoire.
- `append(event)` : persiste un nouvel event valide.

Le service ne sait pas si les events sont stockes dans PostgreSQL via SeaORM ou dans un `HashMap` en memoire. `SeaOrmMatchRepository` et `InMemoryMatchRepository` implementent tous les deux ce trait.

**`MatchStatsRepository`** (`application/repository.rs`)

Port de lecture des statistiques materialisees. Il expose trois operations :

- `read_summary(match_id)` : retourne un `MatchSummary` pre-calcule.
- `read_team_stats(match_id, team_id)` : retourne les stats d'une equipe.
- `read_player_stats(match_id, player_id)` : retourne les stats d'un joueur.

Ce port est volontairement separe de `MatchRepository` (Interface Segregation). Les services de lecture ne dependent pas des operations d'ecriture. En production, l'adapter appelle des fonctions stockees PostgreSQL ; en test, l'adapter en memoire recalcule les stats a la volee.

**`MatchQueryService`** (`application/query_service.rs`)

Port de facade pour les cas d'usage de lecture (CQRS read side). Les handlers HTTP `GET /summary`, `GET /team-stats`, `GET /player-stats` appellent ce trait. `MatchReadService` est l'implementation concrete, mais les handlers ne la voient pas directement.

```
async fn get_summary(match_id) -> Option<MatchSummary>
async fn get_team_stats(match_id, team_id) -> Option<TeamStats>
async fn get_player_stats(match_id, player_id) -> Option<PlayerStats>
```

**`DomainEventPublisher`** (`application/event_publisher.rs`)

Port sortant pour la diffusion d'events metier apres persistence. Apres qu'un event est valide et stocke, le service applicatif appelle `publish`. Aujourd'hui, `NoOpPublisher` est injecte (pas de bus d'events actif). Remplacer par un vrai publisher Kafka ou HTTP ne necessite qu'une nouvelle struct qui implemente ce trait.

```
async fn publish(&self, event: &DomainEvent) -> ApplicationResult<()>
```

**`RecordMatchProvider`** (`application/record_match_provider.rs`)

Port sortant vers une source externe de donnees de match pre-enregistres. Il permet de recuperer un `RecordMatchFeed` depuis un service externe. L'infrastructure fournit `HttpRecordMatchClient` comme adapter concret. Le service applicatif reste agnostique du protocole HTTP utilise pour l'appel.

```
async fn fetch_match(&self, match_id: &str) -> ApplicationResult<Option<RecordMatchFeed>>
```

**`MatchReadModelRepository`** (`application/read_model_repository.rs`)

Port de lecture/ecriture pour les read models mis en cache (`MatchSummary` et `PlayerMatchStats`). Il permet au service de charger des projections pre-calculees sans rejouer l'agregat, et de les mettre a jour apres un nouvel event.

```
async fn load_read_models(match_id) -> Option<CachedMatchReadModels>
async fn save_read_models(match_id, summary, player_stats)
```

#### Pourquoi le service applicatif ne connait ni SeaORM, ni Axum, ni PostgreSQL

`MatchSummaryService` est parametrique sur `R: MatchRepository`. Il recoit ses dependances par injection au moment de la construction (dans `main.rs` ou le bootstrap). Il ne contient aucun `use sea_orm::*`, aucun `use axum::*`, aucun SQL.

Si une requete SQL change, seul `SeaOrmMatchRepository` est modifie. Si le framework HTTP change, seuls les handlers changent. Le service applicatif, lui, reste intact : les cas d'usage metier ne sont pas recompiles ni retestes a cause d'un changement de base de donnees.

### Adapters entrants

Les adapters entrants vivent dans l'infrastructure :

- routes et handlers HTTP ;
- `Consumer` ;
- validation JSON Schema ;
- registre de mappers ;
- mappers d'events ;
- DTO payloads.

Leur role est de recevoir l'entree externe, la valider, la traduire, puis appeler la couche application.

Ils ne portent pas les regles metier du match.

### Adapter de persistence

`SeaOrmMatchRepository` est l'adapter de persistence. Il implemente :

- `MatchRepository`
- `MatchStatsRepository`

La couche application voit uniquement des ports, tandis que le repository gere les details PostgreSQL et SeaORM.

Le repository en memoire implemente les memes ports pour les tests et le replay local.

### Logique stockee en base

Les statistiques sont volontairement traitees via des fonctions stockees PostgreSQL :

- `apply_match_event_stats(event_json JSONB)`
- `get_match_team_stats(p_match_id UUID, p_team_id UUID)`
- `get_match_player_stats(p_match_id UUID, p_player_id UUID)`

Le flux d'ecriture stocke l'event puis appelle `apply_match_event_stats`. Le flux de lecture appelle `get_match_team_stats` ou `get_match_player_stats`.

Ce choix garde la projection statistique proche du modele de persistence, tout en preservant la frontiere applicative via `MatchStatsRepository`.

### Pas d'event metier sortant aujourd'hui

Le diagramme d'infrastructure peut montrer des adapters techniques comme un logger ou un client HTTP, mais ce ne sont pas des events metier sortants.

Les events metier sortants seraient documentes dans `events/outbound/`. Ce dossier est vide aujourd'hui car le contexte ne publie pas d'event metier.

## Choix SOLID

### Single Responsibility Principle

Chaque couche a une responsabilite claire :

- les handlers HTTP lisent les parametres et construisent les reponses HTTP ;
- `Consumer` orchestre la validation et le mapping du JSON entrant ;
- les mappers traduisent les DTO payloads en events domaine ;
- `MatchSummaryService` orchestre le cas d'usage commande ;
- `MatchReadService` orchestre les cas d'usage de lecture ;
- `MatchAggregate` porte les invariants metier ;
- `SeaOrmMatchRepository` gere la persistence et les appels aux fonctions stockees ;
- `DatabaseMigrator` gere la creation des tables et fonctions SQL.

### Open/Closed Principle

Un nouveau type d'event peut etre ajoute en etendant le catalogue, le schema, le DTO et le mapper, sans reecrire le service applicatif.

Une nouvelle implementation de persistence peut etre ajoutee en implementant :

- `MatchRepository`
- `MatchStatsRepository`

Les services applicatifs restent fermes a la modification et ouverts a l'extension via les ports.

### Liskov Substitution Principle

`InMemoryMatchRepository` et `SeaOrmMatchRepository` peuvent etre utilises via les memes traits repository.

Les tests peuvent utiliser l'adapter en memoire tandis que la production utilise SeaORM/PostgreSQL, sans modifier le code applicatif.

### Interface Segregation Principle

Les interfaces repository sont separees :

- `MatchRepository` gere le chargement commande et l'ajout d'events ;
- `MatchStatsRepository` gere la lecture des stats.

Cela evite aux services de lecture de dependre d'operations d'ecriture dont ils n'ont pas besoin.

### Dependency Inversion Principle

La politique de haut niveau vit dans les couches application et domaine. Les details techniques vivent dans l'infrastructure.

Le sens des dependances est :

```text
Infrastructure -> Application -> Domain
```

La couche application ne depend pas d'Axum, SeaORM ou PostgreSQL. A l'inverse, l'infrastructure implemente les ports applicatifs.

## Explication du diagramme de sequence

[event-and-stats-sequence.puml](event-and-stats-sequence.puml) montre le scenario principal d'execution.

### Ingestion d'event

1. Le client envoie `POST /events`.
2. Le handler HTTP transmet le JSON brut au `Consumer`.
3. `Consumer` valide l'enveloppe `BaseEvent` et le payload.
4. `MapperRegistry` selectionne le mapper adapte.
5. Le mapper construit un `DomainEvent` type.
6. `MatchSummaryService` recharge les events precedents via `MatchRepository`.
7. Le repository reconstruit l'agregat depuis l'event store.
8. `MatchAggregate` valide le nouvel event.
9. Le repository ajoute l'event.
10. Le repository appelle `apply_match_event_stats`.
11. L'API retourne `202 Accepted`.

### Requete de resume

1. Le client appelle `GET /matches/{matchId}/summary`.
2. `MatchReadService` demande le resume au repository.
3. Le repository recharge les events et rejoue l'agregat.
4. L'agregat retourne `MatchSummary`.

### Requetes de stats

Les stats equipe et joueur ne sont pas recalculees dans les handlers HTTP.

Elles passent par :

- `MatchReadService` ;
- `MatchStatsRepository` ;
- les fonctions stockees en base.

Cela permet aux routes de stats de rester compatibles avec l'architecture hexagonale tout en utilisant des procedures/fonctions stockees.

## Pourquoi les diagrammes sont decoupes

Le diagramme de classe complet etait trop large pour certains previewers PlantUML locaux. Le decoupage en domaine, application et infrastructure rend chaque diagramme lisible et respecte les frontieres d'architecture.

Ce decoupage est aussi plus facile a defendre dans un rapport :

- un diagramme pour le modele domaine ;
- un diagramme pour les cas d'usage et les ports ;
- un diagramme pour les adapters ;
- un diagramme de sequence pour le comportement principal a l'execution.
