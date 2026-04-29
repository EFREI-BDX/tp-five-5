# SaveMade

**Resume metier**

Evenement outbound d'arret realise par un gardien.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "SAVE_MADE",
  "occurredAt": "2024-11-15T20:05:13.000Z",
  "matchTime": {
    "minute": 5,
    "second": 13,
    "period": "FIRST_HALF"
  },
  "payload": {
    "keeperId": "uuid-v4",
    "relatedShotEventId": "uuid-v4"
  }
}
```

**Champs principaux**

- `payload.keeperId` - gardien ayant realise l'arret
- `payload.relatedShotEventId` - reference au tir concerne
