# Match

**Résumé métier**

Représentation d'un match de five dans le contexte Record Match. Un Match oppose deux équipes distinctes et sert de support aux événements enregistrés pendant la rencontre.

**Attributs persistés en base / JSON**

- **matchId** - identifiant unique du match
- **team1Id** - identifiant de la première équipe participant au match
- **team2Id** - identifiant de la deuxième équipe participant au match
- **startedAt** - date et heure de début du match (timestamp ISO-8601)
- **scheduledDurationMinutes** - durée totale prévue du match en minutes (ex : 40)

**Attributs domaine**

- **matchId** - identifiant unique du match, représenté par un `MatchId`
- **teams** - équipes participant au match, représentées par un `MatchTeams`
- **startedAt** - horodatage de début, représenté par un `OccuredAt`
- **scheduledDurationMinutes** - durée prévue, utilisée pour calculer `MatchTime` (période FIRST_HALF / SECOND_HALF)

**Invariants**

- **matchId** doit être un UUID valide et non vide
- **team1Id** doit être un UUID valide et non vide
- **team2Id** doit être un UUID valide et non vide
- **team1Id** et **team2Id** doivent référencer deux équipes différentes (`CHECK team1_team2_different`)
- **startedAt** doit être un datetime ISO-8601 valide et non nul
- **scheduledDurationMinutes** doit être un entier strictement positif

**Value Objects utilisés**

- `MatchId`
- `TeamId`
- `MatchTeams`
- `OccuredAt`
- `MatchTime`

**Format JSON attendu**

- **Schéma** : `tests/schemas/match.schema.json`
- **Fixture valide** : `tests/fixtures/match.valid.json`
- **Fixture invalide** : `tests/fixtures/match.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un matchId, team1Id, team2Id, startedAt et scheduledDurationMinutes valides ne lève pas d'exception.
- **createInvalidIdMatchThrows** - matchId non UUID lève une exception métier.
- **createInvalidIdTeamThrows** - team1Id ou team2Id non UUID lève une exception métier.
- **createSameTeamsThrows** - team1Id égal à team2Id lève une exception métier.
- **createNullStartedAtThrows** - startedAt nul lève une exception métier.
- **createInvalidDurationThrows** - scheduledDurationMinutes négatif ou nul lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.