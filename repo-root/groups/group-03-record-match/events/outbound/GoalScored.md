# GoalScored

**Resume metier**

Evenement outbound de but valide.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "GOAL_SCORED",
  "occurredAt": "2024-11-15T20:07:43.000Z",
  "matchTime": {
    "minute": 7,
    "second": 43,
    "period": "FIRST_HALF"
  },
  "payload": {
    "scoringTeamId": "uuid-v4",
    "scorerId": "uuid-v4"
  }
}
```

**Champs principaux**

- `payload.scoringTeamId` - equipe creditee du but
- `payload.scorerId` - joueur buteur
