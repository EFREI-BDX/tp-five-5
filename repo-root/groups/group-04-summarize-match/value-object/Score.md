# Value Object — Score

**Résumé métier**

`Score` encapsule le score final d'un match (home/away).

**Attributs**

- **home** — *u32* — nombre de buts de l'équipe à domicile.
- **away** — *u32* — nombre de buts de l'équipe visiteuse.

**Invariants**

- Les valeurs doivent être >= 0 (type `u32` garantit non-négativité).
- Pas d'autres invariants métier pour l'instant (ex: pas de max imposé).

**Format JSON attendu**

- Représentation JSON: objet `{ "home": 2, "away": 1 }`.

**Schéma / Fixtures**

- Le VO est représenté au sein du schéma `MATCH_FINISHED` (champ `finalScore`).

**Tests minimaux attendus**

- Sérialisation/désérialisation round-trip via `serde`.
- Utilisation dans `MatchFinished` et vérification de la comparaison avec le score calculé.

**Références de code**

- Définition: `summarize-match/src/domain/value_objects.rs`
- Usage: `summarize-match/src/application.rs`
