# Value Object — EventId

**Résumé métier**

`EventId` représente l'identifiant unique d'un événement dans la timeline d'un match.

**Attributs**

- **value** — *String* — UUID encapsulé dans `EventId(pub String)`.

**Invariants**

- La valeur doit être un UUID valide.
- La validation est appliquée à la désérialisation du VO dans `summarize-match/src/domain/value_objects.rs`.

**Format JSON attendu**

- Représentation JSON: une chaîne, par exemple `"650e8400-e29b-41d4-a716-446655440001"`.

**Schéma / Fixtures**

- Utilisé dans les champs de corrélation comme `relatedFoulEventId`, `relatedShotEventId` et `cancelledGoalEventId`.
- Validé dans les schemas d'events via `format: "uuid"`.

**Références de code**

- Définition: `summarize-match/src/domain/value_objects.rs`
- Mapping: `summarize-match/src/infrastructure/inbound/mapper_registry.rs`
