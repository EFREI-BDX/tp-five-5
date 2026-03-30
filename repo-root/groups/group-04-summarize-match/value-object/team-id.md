# TeamId

**Résumé métier**

Identifiant unique d’une équipe participante à un match. C’est une chaîne de caractères utilisée pour référencer une `Team` dans un `Match`, attribuer des `Event` à une équipe et agréger des statistiques d’équipe dans le `Summary`.

**Attributs**

- **value** — *UUID*

**Invariants**

- `value` doit être un UUID valide.
- `value` doit être généré automatiquement lors de la création d'une nouvelle instance de `TeamId`.
- `value` ne doit jamais être modifié après création (immuable).

**Format JSON attendu**

- **Type** : `string` (UUID)

- **Schéma** : `tests/schemas/team-id.schema.json`
- **Fixture valide** : `tests/fixtures/team-id.valid.json`
- **Fixture invalide** : `tests/fixtures/team-id.invalid.json`

**Tests minimaux attendus**

- Accepter un UUID valide.
- Rejeter :
  - une valeur vide (`""`),
  - une valeur non UUID (`"team_123"`),
  - `null` / champ absent.
- Vérifier l’immuabilité (si implémenté en code) : impossible de changer `value`.

**Génération des fixtures**

- À définir : ajouter un script/test qui génère un UUID valide pour `team-id.valid.json` et une valeur invalide (ex: `"not-a-uuid"`) pour `team-id.invalid.json`.