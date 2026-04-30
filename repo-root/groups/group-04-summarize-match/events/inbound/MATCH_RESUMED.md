# MATCH_RESUMED

## Role

Event de reprise du match apres une interruption.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_RESUMED",
  "occurredAt": "2024-11-15T20:25:00.000Z",
  "matchTime": { "minute": 20, "second": 0, "period": "SECOND_HALF" },
  "payload": { "reason": "HALF_TIME_END" }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `reason` | `string (enum)` | ✅ | `HALF_TIME_END`, `INCIDENT_RESOLVED`, `OTHER` |

## Regles metier

- Doit suivre une interruption valide.
- Reprend la timeline a partir de l'instant renseigne dans `matchTime`.
