# FoulCommitted

**Resume metier**

Evenement outbound de faute sifflee.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "FOUL_COMMITTED",
  "occurredAt": "2024-11-15T20:09:00.000Z",
  "matchTime": {
    "minute": 9,
    "second": 0,
    "period": "FIRST_HALF"
  },
  "payload": {
    "playerId": "uuid-v4",
    "teamId": "uuid-v4",
    "againstPlayerId": "uuid-v4"
  }
}
```

**Champs principaux**

- `payload.playerId` - joueur fautif
- `payload.teamId` - equipe du fautif
- `payload.againstPlayerId` - joueur victime
