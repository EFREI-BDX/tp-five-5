# Analyse DDD hexagonale - summarize-match

Etat observe le 2026-04-29 sur le code Rust de `summarize-match/src`.

## Vue generale

Le service suit une architecture hexagonale assez nette :

- `domain` contient le coeur metier : value objects, events, read models et agregat `MatchAggregate`.
- `application` contient les cas d'usage et les ports : `ApplicationService`, `MatchRepository`, `DomainEventPublisher`, `MatchQueryService`.
- `infrastructure/inbound` contient les adapters entrants : HTTP Axum, validation JSON Schema, consumer JSON, DTO inbound et mappers vers le domaine.
- `infrastructure/repositories` contient les adapters de persistence : repository memoire et repository SeaORM.
- `infrastructure/outbound` contient les adapters sortants : publication no-op et logging.

## Coeur DDD

L'agregat racine est `MatchAggregate`. Il reconstruit l'etat d'un match par replay de `DomainEvent` et applique les regles metier :

- un match doit commencer avant les actions de jeu ;
- un match termine, annule ou forfait est terminal ;
- le score final doit correspondre au score calcule ;
- un joueur expulse ne peut plus produire certaines actions ;
- les resumes (`MatchSummary`) sont derives de l'agregat.

Les identifiants (`PlayerId`, `TeamId`, `EventId`, `MatchId`) sont des value objects transparents autour de `String`, avec validation UUID a la deserialisation ou a la construction selon le type.

## Hexagone applicatif

`MatchSummaryService<R: MatchRepository>` implemente le port entrant `ApplicationService`.
Il orchestre le flux commande :

1. charger l'agregat depuis `MatchRepository`;
2. appliquer l'evenement domaine;
3. persister l'evenement;
4. publier l'evenement domaine.

`MatchReadService<R: MatchRepository>` implemente le port entrant query `MatchQueryService` en rejouant les evenements puis en produisant `MatchSummary`.

## Adapters

Adapter entrant principal :

- `Consumer<S: ApplicationService>` valide le JSON via `EventSchemaValidator`, parse `BaseEvent`, valide le payload specifique, mappe vers `DomainEvent`, puis appelle le service applicatif.

Adapters HTTP :

- `POST /events` passe par le consumer.
- `GET /matches/{match_id}/summary` passe par `MatchQueryService`.

Adapters sortants :

- `LoggingPublisher` implemente `DomainEventPublisher`.
- `NoOpEventPublisher` et `NoOpPublisher` servent de implementations neutres.

Persistence :

- `InMemoryMatchRepository` conserve les evenements en memoire.
- `SeaOrmMatchRepository` persiste/recharge les evenements depuis la table `match_events`.
- `DatabaseMigrator` cree la table et l'index necessaires.

## Points d'attention

Plusieurs fichiers source existent mais ne sont pas cables dans les modules Rust compiles :

- `domain/record_match.rs`
- `application/record_match_provider.rs`
- `application/read_model_repository.rs`
- `infrastructure/outbound/record_match_http_client.rs`
- `infrastructure/repositories/match_read_model_entity.rs`

Ils sont donc representes dans le diagramme comme `<<non cable>>`. Cela permet de garder le diagramme complet par rapport aux fichiers presents, tout en distinguant clairement le perimetre reel compile et teste.

## Diagramme

Le diagramme de classes complet est disponible ici :

`groups/group-04-summarize-match/class-diagram-ddd-hexagonal.puml`

Validation effectuee :

```txt
cargo test
31 unit tests + 17 integration tests OK, 1 test Neon ignore.
```
