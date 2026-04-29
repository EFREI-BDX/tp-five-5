# Guide — Processus réutilisable pour ajouter un événement

Ce document décrit un processus reproductible (schema-first, TDD) pour ajouter un événement consommé par `resume-match` (`summarize-match`). Il formalise où placer les artefacts (JSON Schema, fixtures, docs), comment dériver les Value Objects (VO), et comment intégrer code + tests en respectant DDD, Hexagonal, TDD et SOLID.

**Principes clés**
- **Domain First**: la logique métier et les Value Objects vivent dans `src/domain.rs` (ou modules `domain/`) — ils ne doivent pas dépendre d'infrastructure.
- **Schema-First**: écrire un JSON Schema (draft 2020-12) pour le `payload` + fixture(s) valides/invalides avant d'implémenter le mapper.
- **Infra Isolée**: mappers, validation JSON Schema et cache appartiennent à `infrastructure/inbound`.
- **TDD**: tests d'abord — écris un test d'intégration Consumer qui échoue (schema valide -> mapping -> dispatch to ApplicationService).
- **SRP / SOLID**: une responsabilité par module (SchemaRegistry, MapperRegistry, ApplicationService, Domain).

**Emplacement des fichiers**
- Schémas: `groups/group-04-summarize-match/tests/schemas/<event>.schema.json`
- Fixtures: `groups/group-04-summarize-match/tests/fixtures/<event>.valid.json` et `...invalid.json`
- Docs événement: `groups/group-04-summarize-match/events/inbound/<EVENT>.md`
- VO docs: `groups/group-04-summarize-match/value-object/<VO>.md`
- Mapper: `summarize-match/src/infrastructure/inbound/mapper_registry.rs` (implémentation + enregistrement)
- Domain types: `summarize-match/src/domain.rs`
- Application logic: `summarize-match/src/application.rs`
- Tests intégration: `summarize-match/tests/integration_consumer.rs`

**Template rapide pour un flux TDD (par événement)**
1. Créer le fichier de doc minimal: `events/inbound/<EVENT>.md` (rôle, payload recommandé, cas obligatoires).
2. Écrire le JSON Schema `tests/schemas/<event>.schema.json` (mettre `$id`, `type: object`, `required: [...]`, `properties: {...}`), choisir `format: "uuid"` pour ids.
3. Ajouter fixtures: `tests/fixtures/<event>.valid.json` et au moins un `...invalid.json` ciblant une violation importante.
4. Écrire un test d'intégration `consumer_validates_<event>` qui: lit la fixture, passe par `Consumer::process_json`, et asserte que le service reçoit l'événement (ou que l'échec se produit pour invalid fixture).
   - Écrire d'abord le test en supposant que le mapper est enregistré et l'ApplicationService existant.
5. Implémenter le mapper dans `infrastructure/inbound/mapper_registry.rs`:
   - DTO -> structure locale (strings/UUIDs) -> valider/convertir en VO du domaine.
   - Mappe vers `DomainEvent::<EventName>`.
   - Enregistrer via `MapperRegistry::with_defaults()`.
6. Implémenter/ajouter le type dans `src/domain.rs` (VOs et event struct). VOs: immuables, validation UUID dans leur `Deserialize` ou via construction.
7. Implémenter règles applicatives spécifiques (si nécessaire) dans `MatchSummaryService`.
8. Exécuter les tests, corriger jusqu'à vert.

**Value Objects (comment les dériver depuis le schema)**
- Identifie les champs récurrents et sémantiques: `playerId`, `teamId`, `eventId`, `quantity`, `score`, etc.
- Crée un VO par concept dans `src/domain.rs` (ex: `PlayerId(pub String)`), avec:
  - `Deserialize` personnalisé qui valide `Uuid::parse_str` lorsqu'il s'agit d'un UUID;
  - `Serialize`/`Deserialize` si nécessaire pour tests/fixtures;
  - `Eq`/`Hash` si utilisé en clé (ex: expulsions set).
- Avantage: cohérence, invariants poussés à la construction (fail-fast), meilleure expressivité en DDD.

**JSON Schema / Fixtures — règles pratiques**
- Utiliser `draft-2020-12`. Toujours ajouter `$id` unique pour faciliter les références.
- `required` contient les champs nécessaires; `additionalProperties: false` par sécurité.
- Examples path:
  - `tests/schemas/<event>.schema.json`
  - `tests/fixtures/<event>.valid.json` — fixture la plus simple qui passe la validation.
  - `tests/fixtures/<event>.invalid.missing-field.json` — montre l'échec attendu.

**Mapper / Validation flow**
- Consumer reçoit message string -> parse JSON `BaseEvent` DTO (transport-level).
- Valide l'envelope `BaseEvent` via `SchemaRegistry` (cache de validateurs précompilés).
- Récupère le `payload` et valide (optionnel si on veut double sécurité) via per-event schema (ou faire `BaseEvent` schema qui $ref le bon payload schema).
- Mapper: `serde_json::from_value(payload)` dans une struct de payload infra (strings), puis convertir en VOs (UUID parsing ici), construire `DomainEvent`.
- ApplicationService reçoit `DomainEvent` — seul le domain/app layer touche les invariants métiers.

**Tests recommandés**
- Unit tests:
  - VO: parsing UUID valide/invalid.
  - Mapper: payload JSON -> DomainEvent.
  - Application rules: `MatchSummaryService` behavior (start -> goal -> finish, red card expulsion behavior).
- Integration tests:
  - Consumer end-to-end with fixture and `TestService` asserting received DomainEvent or expected rejection.

**Checklist à appliquer à chaque nouvel événement**
- [ ] Fiche MD `events/inbound/<EVENT>.md`
- [ ] JSON Schema `tests/schemas/<event>.schema.json` (with $id)
- [ ] Fixtures: valid + invalid
- [ ] Mapper + register
- [ ] Domain types / VO updates
- [ ] Application rules (if affects match state)
- [ ] Unit tests + integration consumer tests

**Conseils d'architecture et pratiques**
- Garder le domaine pur (pas de dépendance à serde/jsonschema). Les VOs peuvent avoir `Deserialize` pour faciliter tests, mais la logique métier accepte uniquement types de domaine.
- Eviter de garder des verrous (`MutexGuard`) sur des points d'attente `await` — clone les petites données avant `await`.
- Centraliser utilitaires (ex: `SchemaRegistry`, `MapperRegistry`) pour éviter duplication.
- Tests TDD: écrire d'abord le test d'intégration qui décrit le contract attendu (fixture + comportement), puis coder mapper/domain.

**Exemples de commandes utiles**
- Lancer les tests: `cd summarize-match && cargo test`.
- Ré-exécuter un test unique: `cargo test name_of_test -- --nocapture`.

**Dernières notes**
- Si tu veux, je génère un template JSON Schema et fixtures (valide/invalid) pour un événement choisi, et un squelette de mapper automatique.

---

Fichier créé: `groups/group-04-summarize-match/events/PROCESS_EVENTS.md`

Souhaites-tu que je crée maintenant les schémas/fixtures + mapper pour `PASS_ATTEMPTED`, `SHOT_ATTEMPTED` et `FOUL_COMMITTED` (flux TDD complet) ?
