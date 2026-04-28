# GOAL_CANCELLED

## Role

Event d'annulation d'un but annonce precedemment.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "GOAL_CANCELLED",
  "occurredAt": "2024-11-15T20:07:55.000Z",
  "matchTime": { "minute": 7, "second": 55, "period": "FIRST_HALF" },
  "payload": {
    "cancelledGoalEventId": "uuid-v4-du-GOAL_SCORED-annule",
    "reason": "OFFSIDE"
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `cancelledGoalEventId` | `string (uuid-v4)` | ✅ | Reference au `GOAL_SCORED` annule |
| `reason` | `string (enum)` | ✅ | `OFFSIDE`, `FOUL`, `OTHER` |

## Regles metier

- L'event annule le but dans la timeline recalculée.
- Le score doit etre recalcule en consequence.
