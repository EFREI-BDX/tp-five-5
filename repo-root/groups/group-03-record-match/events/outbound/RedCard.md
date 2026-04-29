# RedCard

**Resume metier**

Evenement outbound d'expulsion.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "RED_CARD",
  "occurredAt": "2024-11-15T20:22:10.000Z",
  "matchTime": {
    "minute": 22,
    "second": 10,
    "period": "SECOND_HALF"
  },
  "payload": {
    "playerId": "uuid-v4",
    "teamId": "uuid-v4"
  }
}
```

**Champs principaux**

- `payload.playerId` - joueur expulse
- `payload.teamId` - equipe du joueur

**Contrainte**

- apres cet evenement, tout evenement de jeu referencant ce `playerId` devient invalide
