# Value Object — MatchId

**Resume metier**

`MatchId` represente l'identifiant unique d'un match dans le contexte `summarize-match`.

**Attributs**

- **value** — *String* — UUID encapsule dans `MatchId(pub String)`.

**Invariants**

- La valeur doit etre un UUID valide.
- Validation appliquee a la construction via `MatchId::new()` et a la deserialisation serde.

**Format JSON attendu**

- Representation JSON : une chaine (ex: `"11111111-2222-3333-4444-555555555555"`).

**Constructeur domaine**

```rust
MatchId::new(s: impl Into<String>) -> Result<MatchId, String>
MatchId::as_str(&self) -> &str
```

**Schema / Fixtures**

- Utilise dans `BaseEvent.schema.json` (champ `matchId`, `format: "uuid"`).
- Present dans toutes les fixtures d'evenements inbound.

**References de code**

- Definition + validation: `summarize-match/src/domain/value_objects.rs`
- Usage: champ `matchId` de `BaseEvent`, parametre de `MatchRepository::load`, `MatchQueryService::get_summary`
