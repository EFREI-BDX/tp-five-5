# Entity — GoalEntry

**Resume metier**

`GoalEntry` represente un but visible dans le read model `MatchSummary`.

Ce n'est pas une entite persistante autonome : elle est derivee par `MatchAggregate` a partir de `GOAL_SCORED` et mise a jour par `GOAL_CANCELLED`.

**Attributs**

- **event_id** — *String* — identifiant de l'event de but.
- **scoring_team_id** — *TeamId* — equipe a laquelle le but est attribue.
- **scorer_id** — *PlayerId* — joueur buteur.
- **assist_id** — *Option<PlayerId>* — passeur decisif eventuel.
- **is_own_goal** — *bool* — indique un but contre son camp.
- **match_time** — *MatchTime* — temps de jeu du but.
- **cancelled** — *bool* — indique si le but a ete annule.

**Identite**

L'identite est portee par `event_id`, qui correspond au `GOAL_SCORED.event_id`.

**Invariants**

- `scoring_team_id` doit etre l'equipe domicile ou exterieure du match.
- Un joueur expulse ne peut pas marquer.
- Le passeur, s'il existe, ne doit pas etre expulse.
- Un but annule doit referencer un `event_id` de but connu.
- Un but annule diminue le score calcule et passe `cancelled` a `true`.

**Cycle de vie**

- Cree lors de `GOAL_SCORED`.
- Marque comme annule lors de `GOAL_CANCELLED`.
- Expose dans `MatchSummary.goals`.
- Reconstruit par replay des events, pas stocke dans une table separee.

**References de code**

- Definition: `summarize-match/src/domain/summary.rs`
- Creation / annulation: `summarize-match/src/domain/aggregate.rs`
- Events sources: `GOAL_SCORED`, `GOAL_CANCELLED`
