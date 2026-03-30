# PlayerId

**Résumé métier**  
Identifiant unique d’un joueur participant à un match. C’est une chaîne de caractères utilisée pour référencer un `Player` dans la feuille de match et dans les évènements (buteur, passeur, sanction, substitution, etc.).

**Attributs**

- **value** — *UUID*

**Invariants**

- `value` doit être un UUID valide.
- `value` doit être généré automatiquement lors de la création d'une nouvelle instance de `PlayerId`.
- `value` ne doit jamais être modifié après création (immuable).

**Format JSON attendu**

- **Type** : `string` (UUID)

- **Schéma** : `tests/schemas/player-id.schema.json`
- **Fixture valide** : `tests/fixtures/player-id.valid.json`
- **Fixture invalide** : `tests/fixtures/player-id.invalid.json`

**Tests minimaux attendus**

- Accepter une valeur UUID valide.
- Rejeter :
  - une valeur vide (`""`),
  - une valeur non UUID (`"player_123"`),
  - `null` / champ absent.
- Vérifier l’immuabilité (si implémenté en code) : impossible de changer `value`.

**Génération des fixtures**

- À définir : générer un UUID valide pour `player-id.valid.json` et une valeur invalide (ex: `"not-a-uuid"`) pour `player-id.invalid.json`.