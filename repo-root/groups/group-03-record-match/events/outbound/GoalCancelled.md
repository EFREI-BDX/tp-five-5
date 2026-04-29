# GoalCancelled

**Resume metier**

Evenement outbound d'annulation d'un but precedemment signale.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "GOAL_CANCELLED",
  "occurredAt": "2024-11-15T20:07:55.000Z",
  "matchTime": {
    "minute": 7,
    "second": 55,
    "period": "FIRST_HALF"
  },
  "payload": {
    "cancelledGoalEventId": "uuid-v4-du-GOAL_SCORED-annule"
  }
}
```

**Champs principaux**

- `payload.cancelledGoalEventId` - reference vers le but annule
