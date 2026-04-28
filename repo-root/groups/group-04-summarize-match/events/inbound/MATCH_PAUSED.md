# MATCH_PAUSED

## Role

Event d'interruption temporaire du match.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_PAUSED",
  "occurredAt": "2024-11-15T20:20:00.000Z",
  "matchTime": { "minute": 20, "second": 0, "period": "FIRST_HALF" },
  "payload": { "reason": "HALF_TIME" }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `reason` | `string (enum)` | ✅ | `HALF_TIME`, `INCIDENT`, `OTHER` |

## Regles metier

- Doit conserver la coherence de la timeline.
- Peut etre suivi d'un `MATCH_RESUMED` avant la fin du match.
