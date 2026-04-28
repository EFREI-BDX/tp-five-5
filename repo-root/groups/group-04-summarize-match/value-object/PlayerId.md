# Value Object — PlayerId

**Résumé métier**

`PlayerId` représente l'identifiant unique d'un joueur dans le contexte `summarize-match`.

**Attributs**

- **value** — *String* — identifiant opaque (ex: UUID format), stocké dans une newtype Rust `PlayerId(pub String)`.

**Invariants**

- Aucune validation de format n'est appliquée actuellement dans le domaine; l'invariant attendu est que la valeur soit non vide.

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

- Définition: [src/domain.rs](src/domain.rs#L1-L200)
- Usage: `Player.player_id`, `GoalScored.scorer_id` — voir [src/infrastructure/inbound/mapper_registry.rs](src/infrastructure/inbound/mapper_registry.rs)
