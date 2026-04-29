# Value Object — MatchTime

**Résumé métier**

`MatchTime` représente le temps de jeu auquel un événement se produit.

**Attributs**

- **minute** — *u32* — minute de jeu, minimum `0`.
- **second** — *u32* — seconde dans la minute, entre `0` et `59`.
- **period** — *String* — période de jeu (`FIRST_HALF` ou `SECOND_HALF`).

**Invariants**

- `second` doit être entre `0` et `59` (inclus).
- `period` doit être `FIRST_HALF` ou `SECOND_HALF` (toute autre valeur est rejetée).
- La validation est appliquée **à deux niveaux** :
  - Schéma JSON `BaseEvent.schema.json` (frontière inbound, première ligne de défense).
  - `TryFrom<MatchTimeRaw>` dans `src/domain/value_objects.rs` (invariant domaine, garantit que le VO est toujours valide quelle que soit la source de construction).

**Format JSON attendu**

```json
{ "minute": 7, "second": 43, "period": "FIRST_HALF" }
```

**Schéma / Fixtures**

- Défini dans `tests/schemas/BaseEvent.schema.json`.
- Présent dans toutes les fixtures d'événements inbound.

**Constructeur domaine**

```rust
MatchTime::new(minute, second, period) -> Result<MatchTime, String>
```

**Références de code**

- Définition + validation: `summarize-match/src/domain/value_objects.rs`
