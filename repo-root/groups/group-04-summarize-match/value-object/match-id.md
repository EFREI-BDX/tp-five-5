# MatchId

**Résumé métier**

Identifiant unique d’un match. C’est une chaîne de caractères utilisée pour référencer un `Match` et rattacher un `Summary` à ce match.

**Attributs**

- **value** — *UUID*

**Invariants**

- `value` doit être un UUID valide.
- `value` doit être généré automatiquement lors de la création d'une nouvelle instance de `MatchId`.
- `value` ne doit jamais être modifié après création (immuable).

**Format JSON attendu**

- **Type** : `string` (UUID)

- **Schéma** : `tests/schemas/match-id.schema.json`
- **Fixture valide** : `tests/fixtures/match-id.valid.json`
- **Fixture invalide** : `tests/fixtures/match-id.invalid.json`

**Tests minimaux attendus**

- Accepter un UUID v4 valide.
- Rejeter :
  - une valeur vide (`""`),
  - une valeur non UUID (`"match_123"`),
  - `null` / champ absent.
- Vérifier l’immuabilité (si implémenté en code) : impossible de changer `value`.

**Génération des fixtures**

- À définir : ajouter un script/test qui génère un UUID valide pour `*.valid.json` et une valeur invalide (ex: `"not-a-uuid"`) pour `*.invalid.json`.