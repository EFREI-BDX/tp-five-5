# MatchEvent

**Résumé métier**

Représentation d'un événement survenu pendant un match de five. Un MatchEvent associe un match, un type d'événement et éventuellement un ou deux joueurs selon la nature de l'événement. Il permet de reconstruire le déroulé du match et de produire des statistiques.

**Attributs persistés en base / JSON**

- **IdMatch** - identifiant du match concerné par l'événement
- **IdEvent** - identifiant du type d'événement enregistré
- **IdPlayer1** - identifiant du joueur principal impliqué dans l'événement
- **IdPlayer2** - identifiant du joueur secondaire impliqué dans l'événement
- **OccuredAt** - date et heure réelle à laquelle l'événement a été enregistré

**Attributs domaine**

- **idMatch** - identifiant du match concerné, représenté par un `MatchId`
- **idEvent** - identifiant du type d'événement enregistré, représenté par un `EventId`
- **participants** - joueurs impliqués dans l'événement, représentés par un `EventParticipants`
- **occurredAt** - date et heure réelle d'enregistrement, représentée par un `OccurredAt`

**Invariants**

- **idMatch** doit être un UUID valide et non vide
- **idEvent** doit être un UUID valide et non vide
- **occuredAt** doit être une date valide et non vide
- **idPlayer1** est obligatoire si l'événement référencé possède **Event.nbPlayer = 1**
- **idPlayer1** et **idPlayer2** sont obligatoires si l'événement référencé possède **Event.nbPlayer = 2**
- **idPlayer1** et **idPlayer2** doivent être différents lorsqu'ils sont tous les deux renseignés
- Si **Event.nbPlayer = 0**, aucun joueur ne doit être obligatoire
- Les joueurs renseignés doivent appartenir à l'une des deux équipes du match

**Value Objects utilisés**

- `MatchId`
- `EventId`
- `PlayerId`
- `EventParticipants`
- `OccurredAt`
- `MatchTime`

**Format JSON attendu**

- **Schéma** : `tests/schemas/match-event.schema.json`
- **Fixture valide** : `tests/fixtures/match-event.valid.json`
- **Fixture invalide** : `tests/fixtures/match-event.invalid.json`

**Tests minimaux attendus**

- **createValidWithoutPlayer** - construction d'un événement avec Event.nbPlayer = 0 ne nécessite pas de joueur.
- **createValidWithOnePlayer** - construction d'un événement avec Event.nbPlayer = 1 nécessite idPlayer1.
- **createValidWithTwoPlayers** - construction d'un événement avec Event.nbPlayer = 2 nécessite idPlayer1 et idPlayer2.
- **createInvalidMissingPlayer1Throws** - absence de idPlayer1 pour un événement nécessitant au moins un joueur lève une exception métier.
- **createInvalidMissingPlayer2Throws** - absence de idPlayer2 pour un événement nécessitant deux joueurs lève une exception métier.
- **createInvalidSamePlayersThrows** - idPlayer1 égal à idPlayer2 lève une exception métier.
- **createInvalidMatchTimeThrows** - matchTime négatif ou invalide lève une exception métier.
- **createInvalidOccuredAtThrows** - occuredAt vide ou invalide lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.