# MatchForfeited

**Resume metier**

Evenement outbound de forfait d'une equipe.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_FORFEITED",
  "occurredAt": "2024-11-15T20:12:30.000Z",
  "matchTime": {
    "minute": 12,
    "second": 30,
    "period": "FIRST_HALF"
  },
  "payload": {
    "forfeitingTeamId": "uuid-v4"
  }
}
```

**Champs principaux**

- `payload.forfeitingTeamId` - equipe declaree forfaitaire
