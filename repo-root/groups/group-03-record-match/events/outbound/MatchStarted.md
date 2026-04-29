# MatchStarted

**Resume metier**

Evenement outbound de debut de match. Il ouvre la timeline et transporte la feuille de match initiale.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_STARTED",
  "occurredAt": "2024-11-15T20:00:00.000Z",
  "matchTime": {
    "minute": 0,
    "second": 0,
    "period": "FIRST_HALF"
  },
  "payload": {
    "homeTeam": {
      "teamId": "uuid-v4",
      "startingPlayers": [
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"}
      ]
    },
    "awayTeam": {
      "teamId": "uuid-v4",
      "startingPlayers": [
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"},
        { "playerId": "uuid-v4"}
      ]
    },
    "scheduledDurationMinutes": 40
  }
}
```

**Champs principaux**

- `eventId` - identifiant unique de l'event
- `matchId` - identifiant du match concerne
- `type` - valeur fixe `MATCH_STARTED`
- `occurredAt` - horodatage ISO-8601 UTC
- `matchTime` - temps de jeu au coup d'envoi
- `payload.homeTeam` et `payload.awayTeam` - composition initiale des equipes
- `payload.scheduledDurationMinutes` - duree theorique du match

**Contraintes**

- `matchTime.minute` vaut `0`
- `matchTime.second` vaut `0`
- `matchTime.period` vaut `FIRST_HALF`
- chaque equipe declare exactement 5 titulaires
