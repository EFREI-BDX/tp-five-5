# Value Object — PlayerId

**Résumé métier**

`PlayerId` représente l'identifiant unique d'un joueur dans le contexte `summarize-match`.

**Attributs**

- **value** — *String* — identifiant opaque (ex: UUID format), stocké dans une newtype Rust `PlayerId(pub String)`.

**Invariants**

- La valeur doit être un UUID valide.
- La validation est appliquée à la désérialisation du VO dans `summarize-match/src/domain/value_objects.rs`.

**Format JSON attendu**

- Représentation JSON: une chaîne (ex: `"00000000-0000-0000-0000-000000000001"`).

**Schéma / Fixtures**

- Pas de schéma JSON dédié pour le VO isolé; il est encodé via les schémas des events (ex: `tests/schemas/...`).

**Tests minimaux attendus**

- Sérialisation / désérialisation round-trip via `serde`.
- Construction depuis `&str`/`String` via `From`.

**Génération des fixtures**

- Les fixtures d'events incluent des `playerId` (voir `tests/fixtures` dans le groupe). Aucune commande dédiée.

**Références de code**

- Définition: `summarize-match/src/domain/value_objects.rs`
- Usage: `Player.player_id`, `GoalScored.scorer_id` — voir `summarize-match/src/infrastructure/inbound/mapper_registry.rs`
