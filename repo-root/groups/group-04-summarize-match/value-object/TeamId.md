# Value Object — TeamId

**Résumé métier**

`TeamId` représente l'identifiant unique d'une équipe dans le contexte `summarize-match`.

**Attributs**

- **value** — *String* — identifiant opaque (ex: UUID format), encapsulé dans `TeamId(pub String)`.

**Invariants**

- La valeur doit être un UUID valide.
- La validation est appliquée à la désérialisation du VO dans `summarize-match/src/domain/value_objects.rs`.

**Format JSON attendu**

- Représentation JSON: une chaîne (ex: `"aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"`).

**Schéma / Fixtures**

- Le VO n'a pas de schéma isolé — il est utilisé dans les schémas d'events (ex: `tests/schemas/match-started.schema.json`).

**Tests minimaux attendus**

- Sérialisation/désérialisation via `serde`.
- Comparaison d'égalité, clonage et conversion depuis `String`/`&str`.

**Génération des fixtures**

- Voir fixtures d'events sous `tests/fixtures`.

**Références de code**

- Définition: `summarize-match/src/domain/value_objects.rs`
- Usage: `Team.team_id`, `MatchState.home_team_id` — voir `summarize-match/src/application.rs`
