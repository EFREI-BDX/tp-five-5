# PassAttempted

**Resume metier**

Evenement outbound de tentative de passe.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "PASS_ATTEMPTED",
  "occurredAt": "2024-11-15T20:03:05.000Z",
  "matchTime": {
    "minute": 3,
    "second": 5,
    "period": "FIRST_HALF"
  },
  "payload": {
    "passerId": "uuid-v4",
    "teamId": "uuid-v4",
    "receiverId": "uuid-v4",
    "succeeded": true
  }
}
```

**Champs principaux**

- `payload.passerId` - joueur passeur
- `payload.teamId` - equipe du passeur
- `payload.receiverId` - receveur de la passe
- `payload.succeeded` - true si la passe est reussie

**Remarque**

- cet evenement couvre a la fois la tentative et la passe reussie via `succeeded`
