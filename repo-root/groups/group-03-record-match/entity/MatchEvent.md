# MatchEvent

**Résumé métier**

Représentation d'un événement survenu pendant un match de five. Un MatchEvent associe un match, un type d'événement et éventuellement zéro ou un ou deux joueurs selon la nature de l'événement. Il permet de reconstruire le déroulé du match et de produire des statistiques.

**Attributs persistés en base / JSON**

- **matchEventId**- identifiant du match concerné par l'événement
- **matchId** - identifiant du match concerné
- **eventId** - identifiant du type d'événement enregistré
- **player1Id**  - identifiant du joueur principal impliqué dans l'événement
- **player2Id** - identifiant du joueur secondaire impliqué dans l'événement
- **occuredAt** - date et heure réelle à laquelle l'événement a été enregistré

**Attributs domaine**

- **matchEventId** - identifiant unique de l'occurrence
- **matchId** - identifiant du match concerné, représenté par un `MatchId`
- **eventId** - identifiant du type d'événement enregistré, représenté par un `EventId`
- **participants** - joueurs impliqués dans l'événement, représentés par un `EventParticipants`
- **occurredAt** - date et heure réelle d'enregistrement, représentée par un `OccurredAt`

**Invariants**

- **matchEventId** doit être un UUID valide et non vide
- **matchId** doit être un UUID valide et non vide
- **eventId** doit être un UUID valide et non vide
- **occuredAt** doit être une date/heure valide et non vide
- Si `Event.nbPlayers = 0`, alors `player1Id` et `player2Id` doivent être `NULL`
- Si `Event.nbPlayers = 1`, alors `player1Id` est requis et `player2Id` doit être `NULL`
- Si `Event.nbPlayers = 2`, alors `player1Id` et `player2Id` sont requis
- Si `Event.nbPlayers = 2`, `player1Id` et `player2Id` doivent être différents
- `matchId`, `eventId`, `player1Id`, `player2Id` doivent référencer des lignes existantes 

**Value Objects utilisés**

- `EventId`
- `MatchId`
- `PlayerId`
- `EventParticipants`
- `OccuredAt`

**Format JSON attendu**

- **Schéma** : `tests/schemas/match-event.schema.json`
- **Fixture valide** : `tests/fixtures/match-event.valid.json`
- **Fixture invalide** : `tests/fixtures/match-event.invalid.json`

**Tests minimaux attendus**

- **createValidWithoutPlayer** - construction avec `Event.nbPlayers = 0` et `player1Id/player2Id = NULL` ne lève pas d'exception.
- **createValidWithOnePlayer** - construction avec `Event.nbPlayers = 1` nécessite `player1Id` et `player2Id = NULL`.
- **createValidWithTwoPlayers** - construction avec `Event.nbPlayers = 2` nécessite `player1Id` et `player2Id`.
- **createInvalidMissingPlayer1Throws** - absence de `player1Id` pour `Event.nbPlayers = 1` ou `2` lève une exception métier.
- **createInvalidUnexpectedPlayer2Throws** - présence de `player2Id` pour `Event.nbPlayers = 1` lève une exception métier.
- **createInvalidMissingPlayer2Throws** - absence de `player2Id` pour `Event.nbPlayers = 2` lève une exception métier.
- **createInvalidSamePlayersThrows** - `player1Id` égal à `player2Id` pour `Event.nbPlayers = 2` lève une exception métier.
- **createInvalidOccuredAtThrows** - `occuredAt` vide ou invalide lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.