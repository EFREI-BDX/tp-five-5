# Entity — SubstitutionEntry

**Resume metier**

`SubstitutionEntry` represente un remplacement visible dans le read model `MatchSummary`.

Il est derive par `MatchAggregate` depuis l'event `SUBSTITUTION`.

**Attributs**

- **event_id** — *String* — identifiant de l'event de remplacement.
- **team_id** — *TeamId* — equipe qui effectue le remplacement.
- **player_out** — *PlayerId* — joueur sortant.
- **player_in** — *PlayerId* — joueur entrant.
- **match_time** — *MatchTime* — temps de jeu du remplacement.

**Identite**

L'identite est portee par `event_id`.

**Invariants**

- Un remplacement ne peut etre applique que sur un match actif.
- Le joueur sortant ne doit pas etre expulse.
- Le joueur entrant ne doit pas etre expulse.

**Cycle de vie**

- Cree lors de `SUBSTITUTION`.
- Expose dans `MatchSummary.substitutions`.
- Reconstruit par replay des events, pas stocke dans une table separee.

**References de code**

- Definition: `summarize-match/src/domain/summary.rs`
- Creation: `summarize-match/src/domain/aggregate.rs`
- Event source: `SUBSTITUTION`
