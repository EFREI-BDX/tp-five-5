# Value Objects — Summarize Match

**Résumé métier**

Index des *Value Objects* utilisés dans le bounded context **Summarize Match** (Five).  
Ce document sert de table des matières et rappelle le format attendu des VO côté JSON.

**Attributs**

- **matchId** — *MatchId (UUID)*
- **teamId** — *TeamId (UUID)*
- **playerId** — *PlayerId (UUID)*
- **eventTimestamp** — *EventTimestamp (minute/second)*
- **score** — *Score (homeGoals/awayGoals)*

**Invariants**

- Tous les `*Id.value` doivent être des UUID valides.
- Les `*Id.value` sont générés automatiquement lors de la création d’une nouvelle instance.
- `EventTimestamp.minute` ≥ 0.
- `EventTimestamp.second` ∈ [0..59].
- `Score.homeGoals` ≥ 0 et `Score.awayGoals` ≥ 0.

**Format JSON attendu**

- `MatchId`, `TeamId`, `PlayerId` : `string` (UUID)
- `EventTimestamp` : objet `{ minute: number, second: number }`
- `Score` : objet `{ homeGoals: number, awayGoals: number }`

**Schémas / Fixtures**

- À compléter : créer un schéma et des fixtures par VO
  - **Schémas** : `tests/schemas/<vo-name>.schema.json`
  - **Fixtures valides** : `tests/fixtures/<vo-name>.valid.json`
  - **Fixtures invalides** : `tests/fixtures/<vo-name>.invalid.json`

VO concernés (noms de fichiers recommandés) :
- `match-id` → `tests/schemas/match-id.schema.json`, `tests/fixtures/match-id.valid.json`, `tests/fixtures/match-id.invalid.json`
- `team-id` → `tests/schemas/team-id.schema.json`, `tests/fixtures/team-id.valid.json`, `tests/fixtures/team-id.invalid.json`
- `player-id` → `tests/schemas/player-id.schema.json`, `tests/fixtures/player-id.valid.json`, `tests/fixtures/player-id.invalid.json`
- `event-timestamp` → `tests/schemas/event-timestamp.schema.json`, `tests/fixtures/event-timestamp.valid.json`, `tests/fixtures/event-timestamp.invalid.json`
- `score` → `tests/schemas/score.schema.json`, `tests/fixtures/score.valid.json`, `tests/fixtures/score.invalid.json`

**Tests minimaux attendus**

- `*Id` :
  - accepte un UUID valide,
  - rejette `""`, `null`, champ absent, string non UUID.
- `EventTimestamp` :
  - accepte `{ minute: 0, second: 0 }`,
  - rejette `minute < 0`,
  - rejette `second < 0` ou `second > 59`.
- `Score` :
  - accepte `{ homeGoals: 0, awayGoals: 0 }`,
  - rejette valeurs négatives,
  - rejette types invalides (string, float, null).

**Génération des fixtures**

- À définir : un script ou un test qui écrit automatiquement les `*.valid.json` et `*.invalid.json` à partir des contraintes ci-dessus.
- Variante simple (manuelle) : écrire les fixtures à la main en respectant les invariants, puis valider via les tests JSON Schema.