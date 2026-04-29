# Entity — CardEntry

**Resume metier**

`CardEntry` represente un carton visible dans le read model `MatchSummary`.

Il est derive par `MatchAggregate` depuis les events `YELLOW_CARD` et `RED_CARD`.

**Attributs**

- **event_id** — *String* — identifiant de l'event carton.
- **player_id** — *PlayerId* — joueur sanctionne.
- **team_id** — *TeamId* — equipe du joueur sanctionne.
- **match_time** — *MatchTime* — temps de jeu de la sanction.
- **card_type** — *CardType* — type de carton.

**Identite**

L'identite est portee par `event_id`.

**Types de carton**

- `Yellow { card_number: u8 }`
- `Red { is_double_yellow: bool }`

**Invariants**

- Un carton ne peut etre applique que sur un match actif.
- Un carton jaune est refuse pour un joueur deja expulse.
- Un carton rouge ajoute le joueur dans `expelled_players`.
- Apres un carton rouge, les actions de jeu du joueur sont refusees par l'agregat.

**Cycle de vie**

- Cree lors de `YELLOW_CARD` ou `RED_CARD`.
- Expose dans `MatchSummary.cards`.
- Reconstruit par replay des events, pas stocke dans une table separee.

**References de code**

- Definition: `summarize-match/src/domain/summary.rs`
- Creation: `summarize-match/src/domain/aggregate.rs`
- Events sources: `YELLOW_CARD`, `RED_CARD`
