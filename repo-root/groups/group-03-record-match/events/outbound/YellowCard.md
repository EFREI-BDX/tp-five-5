# YellowCard

**Resume metier**

Evenement outbound de carton jaune.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "YELLOW_CARD",
  "occurredAt": "2024-11-15T20:09:05.000Z",
  "matchTime": {
    "minute": 9,
    "second": 5,
    "period": "FIRST_HALF"
  },
  "payload": {
    "playerId": "uuid-v4"
  }
}
```

**Champs principaux**

- `payload.playerId` - joueur sanctionne
