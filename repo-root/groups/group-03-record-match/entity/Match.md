# Match

**Résumé métier**

Représentation d'un match de five dans le contexte Record Match. Un Match oppose deux équipes distinctes et sert de support aux événements enregistrés pendant la rencontre.

**Attributs persistés en base / JSON**

- **IdMatch** - identifiant unique du match
- **IdTeam1** - identifiant de la première équipe participant au match
- **IdTeam2** - identifiant de la deuxième équipe participant au match

**Attributs domaine**

- **idMatch** - identifiant unique du match, représenté par un `MatchId`
- **teams** - équipes participant au match, représentées par un `MatchTeams`

**Invariants**

- **idMatch** doit être un UUID valide et non vide
- **idTeam1** doit être un UUID valide et non vide
- **idTeam2** doit être un UUID valide et non vide
- **idTeam1** et **idTeam2** doivent référencer deux équipes différentes

**Value Objects utilisés**

- `MatchId`
- `TeamId`
- `MatchTeams`

**Format JSON attendu**

- **Schéma** : `tests/schemas/match.schema.json`
- **Fixture valide** : `tests/fixtures/match.valid.json`
- **Fixture invalide** : `tests/fixtures/match.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un idMatch, un idTeam1 et un idTeam2 UUID valides ne lève pas d'exception.
- **createInvalidIdMatchThrows** - idMatch non UUID lève une exception métier.
- **createInvalidIdTeamThrows** - idTeam1 ou idTeam2 non UUID lève une exception métier.
- **createSameTeamsThrows** - idTeam1 égal à idTeam2 lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.