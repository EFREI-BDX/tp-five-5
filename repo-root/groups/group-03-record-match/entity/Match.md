# Match

**Résumé métier**

Représentation d'un match de five dans le contexte Record Match. Un Match oppose deux équipes distinctes et sert de support aux événements enregistrés pendant la rencontre.

**Attributs persistés en base / JSON**

- **matchId** - identifiant unique du match
- **team1Id** - identifiant de la première équipe participant au match
- **team2Id** - identifiant de la deuxième équipe participant au match

**Attributs domaine**

- **matchId** - identifiant unique du match, représenté par un `MatchId`
- **teams** - équipes participant au match, représentées par un `MatchTeams`

**Invariants**

- **matchId** doit être un UUID valide et non vide
- **team1Id** doit être un UUID valide et non vide
- **team2Id** doit être un UUID valide et non vide
- **team1Id** et **team2Id** doivent référencer deux équipes différentes (`CHECK team1_team2_different`)

**Value Objects utilisés**

- `MatchId`
- `TeamId`
- `MatchTeams`

**Format JSON attendu**

- **Schéma** : `tests/schemas/match.schema.json`
- **Fixture valide** : `tests/fixtures/match.valid.json`
- **Fixture invalide** : `tests/fixtures/match.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un matchId, un team1Id et un team2Id UUID valides ne lève pas d'exception.
- **createInvalidIdMatchThrows** - matchId non UUID lève une exception métier.
- **createInvalidIdTeamThrows** - team1Id ou team2Id non UUID lève une exception métier.
- **createSameTeamsThrows** - team1Id égal à team2Id lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.