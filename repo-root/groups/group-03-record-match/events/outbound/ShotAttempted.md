# ShotAttempted

**Resume metier**

Evenement outbound de tentative de tir.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "SHOT_ATTEMPTED",
  "occurredAt": "2024-11-15T20:05:12.000Z",
  "matchTime": {
    "minute": 5,
    "second": 12,
    "period": "FIRST_HALF"
  },
  "payload": {
    "shooterId": "uuid-v4",
    "onTarget": true
  }
}
```

**Champs principaux**

- `payload.shooterId` - joueur tireur
- `payload.onTarget` - vrai si tir cadre
