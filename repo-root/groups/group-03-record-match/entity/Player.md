# Player

**Résumé métier**

Représentation locale d'un joueur de five dans le contexte Record Match. Un Player n'est pas géré ici dans son intégralité, mais il est référencé dans les événements d'un match comme joueur principal ou joueur secondaire. Il est rattaché à une équipe afin de permettre la validation des événements enregistrés pendant un match.

**Attributs persistés en base / JSON**

- **IdPlayer** - identifiant unique du joueur
- **IdTeam** - identifiant de l'équipe à laquelle appartient le joueur

**Attributs domaine**

- **idPlayer** - identifiant unique du joueur, représenté par un `PlayerId`
- **idTeam** - identifiant de l'équipe du joueur, représenté par un `TeamId`

**Invariants**

- **idPlayer** doit être un UUID valide et non vide
- **idTeam** doit être un UUID valide et non vide
- Un joueur doit toujours être rattaché à une équipe

**Value Objects utilisés**

- `PlayerId`
- `TeamId`

**Format JSON attendu**

- **Schéma** : `tests/schemas/player.schema.json`
- **Fixture valide** : `tests/fixtures/player.valid.json`
- **Fixture invalide** : `tests/fixtures/player.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un idPlayer et un idTeam UUID valides ne lève pas d'exception.
- **createInvalidIdPlayerThrows** - idPlayer non UUID lève une exception métier.
- **createInvalidIdTeamThrows** - idTeam non UUID lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.