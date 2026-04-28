# summarize-match (Rust service)

Ce crate implemente l'adaptateur inbound du contexte `resume-match` selon une architecture DDD hexagonale.

## Architecture (courte)

- `src/domain.rs` : evenements et objets metier (`DomainEvent`, `MatchStarted`, `Team`, `Player`).
- `src/application.rs` : port inbound `ApplicationService` + use case `MatchSummaryService`.
- `src/infrastructure/inbound/` :
  - `consumer.rs` : orchestrateur inbound (validation envelope + payload + mapping + dispatch).
  - `dto.rs` : DTO transport (`BaseEvent`).
  - `schema_registry.rs` : cache/chargement des schemas JSON.
  - `mapper_registry.rs` : pattern Strategy/Registry pour mapper `event_type -> DomainEvent`.
- `src/infrastructure/error.rs` : erreurs structurees de validation/adaptateur.

## Flux inbound

1. Parse JSON brut en `serde_json::Value`.
2. Valide l'enveloppe avec `BaseEvent.schema.json`.
3. Deserialize en DTO transport `BaseEvent`.
4. Resolve schema payload par `event_type` (`match-started.schema.json`, etc.).
5. Valide `payload`.
6. Mappe DTO -> `DomainEvent` via `MapperRegistry`.
7. Appelle `ApplicationService::handle_event`.

## Regles actuellement implantees (doc-driven)

- `MATCH_STARTED` est supporte.
- `MATCH_STARTED` doit etre unique par `match_id`.
- Chaque equipe doit avoir exactement un gardien titulaire.

## Ajouter un nouvel event inbound

1. Ajouter/mettre a jour le schema payload dans `../tests/schemas/<event>.schema.json`.
2. Ajouter fixture valide/invalide dans `../tests/fixtures/`.
3. Ajouter un mapper dans `mapper_registry.rs` (ou fichier dedie sous `mappers/`).
4. Enregistrer le mapper dans `MapperRegistry::with_defaults()`.
5. Ajouter tests unitaires mapper + integration consumer.
6. Mettre a jour la doc `events/inbound/<EVENT>.md` si necessaire.

## Lancer les tests

```bash
cargo test
```
