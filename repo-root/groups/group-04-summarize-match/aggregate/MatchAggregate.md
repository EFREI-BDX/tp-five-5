# Aggregate — MatchAggregate

**Resume metier**

`MatchAggregate` represente l'etat metier reconstruit d'un match a partir de sa timeline d'events.

Il est la **racine d'agregat** du bounded context `summarize-match`, pas un simple objet agregat passif.

En vocabulaire DDD :

- **Aggregate** : le match reconstruit avec son score, ses buts, ses cartons, ses remplacements et son statut.
- **Aggregate root** : `MatchAggregate`, point d'entree unique qui controle les modifications de cet aggregate.

Toutes les regles de coherence du match passent par cette racine avant qu'un event soit accepte.

**Responsabilites**

- Servir de point d'entree unique pour modifier l'etat metier du match.
- Rejouer les `DomainEvent` d'un match dans l'ordre.
- Calculer le score courant a partir des buts valides et des buts annules.
- Refuser les actions recues avant `MATCH_STARTED`.
- Refuser les actions recues apres un event terminal.
- Garantir que `MATCH_STARTED` arrive une seule fois.
- Garantir exactement un gardien dans chaque equipe au demarrage.
- Refuser les actions de jeu d'un joueur expulse.
- Gerer les transitions pause/reprise.
- Produire le read model `MatchSummary`.

**Etat interne**

- **started** — *bool* — indique si le match a commence.
- **finished** — *bool* — indique si un event terminal a ete applique.
- **cancelled** — *bool* — indique si le match a ete annule.
- **forfeited** — *bool* — indique si le match a ete perdu par forfait.
- **home_team_id** — *Option<TeamId>* — equipe domicile connue apres `MATCH_STARTED`.
- **away_team_id** — *Option<TeamId>* — equipe exterieure connue apres `MATCH_STARTED`.
- **computed_home_score** — *u32* — score domicile calcule depuis les events.
- **computed_away_score** — *u32* — score exterieur calcule depuis les events.
- **expelled_players** — *HashSet<PlayerId>* — joueurs expulses.
- **paused** — *bool* — indique si le match est actuellement en pause.
- **goal_teams** — *HashMap<String, TeamId>* — association entre un event de but et son equipe, utile pour annuler un but.
- **goals** — *Vec<GoalEntry>* — buts exposes dans le read model.
- **cards** — *Vec<CardEntry>* — cartons exposes dans le read model.
- **substitutions** — *Vec<SubstitutionEntry>* — remplacements exposes dans le read model.
- **match_end_second** — *u32* — instant de fin conserve pour le statut terminal.

**Invariants**

- `MATCH_STARTED` doit etre le premier event metier actif du match.
- `MATCH_STARTED` ne peut etre applique qu'une seule fois.
- Les deux equipes de `MATCH_STARTED` doivent avoir exactement un gardien.
- Un event de jeu actif est refuse si le match n'a pas commence.
- Un event de jeu actif est refuse apres un event terminal.
- `GOAL_SCORED.scoring_team_id` doit appartenir aux equipes du match.
- `GOAL_CANCELLED.cancelled_goal_event_id` doit referencer un but connu et non deja annule.
- `MATCH_FINISHED.final_score` doit correspondre au score calcule.
- `MATCH_PAUSED` est refuse si le match est deja en pause.
- `MATCH_RESUMED` est refuse si le match n'est pas en pause.
- Un joueur expulse ne peut plus faire de passe, tir, faute, but, sauvegarde ou substitution.

**Commandes / methodes domaine**

```rust
MatchAggregate::handle_event(event: DomainEvent) -> anyhow::Result<()>
MatchAggregate::to_summary(match_id: &str) -> MatchSummary
MatchAggregate::is_known() -> bool
```

**Events consommes**

- `MATCH_STARTED`
- `GOAL_SCORED`
- `GOAL_CANCELLED`
- `MATCH_FINISHED`
- `RED_CARD`
- `PASS_ATTEMPTED`
- `SHOT_ATTEMPTED`
- `FOUL_COMMITTED`
- `YELLOW_CARD`
- `SAVE_MADE`
- `SUBSTITUTION`
- `MATCH_PAUSED`
- `MATCH_RESUMED`
- `MATCH_CANCELLED`
- `MATCH_FORFEITED`

**Sorties produites**

- `MatchSummary` pour la route HTTP `GET /matches/{matchId}/summary`.
- Aucun event metier outbound n'est produit aujourd'hui.

**Place dans l'architecture hexagonale**

- Le port applicatif `ApplicationService` appelle l'agregat via `MatchSummaryService`.
- Le port sortant `MatchRepository` recharge les events puis reconstruit l'agregat.
- Les adapters `Consumer`, HTTP et SeaORM ne doivent pas dupliquer les regles de `MatchAggregate`.

**References de code**

- Definition: `summarize-match/src/domain/aggregate.rs`
- Events: `summarize-match/src/domain/events.rs`
- Read model: `summarize-match/src/domain/summary.rs`
- Service applicatif: `summarize-match/src/application/service.rs`
- Repository: `summarize-match/src/application/repository.rs`
