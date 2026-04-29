# SAVE_MADE

## Role

Event d'arret effectue par le gardien.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "SAVE_MADE",
  "occurredAt": "2024-11-15T20:05:13.000Z",
  "matchTime": { "minute": 5, "second": 13, "period": "FIRST_HALF" },
  "payload": {
    "keeperId": "uuid-v4",
    "keeperTeamId": "uuid-v4",
    "relatedShotEventId": "uuid-v4"
  }
}
```

keeperTeamId , relatedShotEventId supprimé 

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `keeperId` | `string (uuid-v4)` | ✅ | Gardien qui realise l'arret |
| `keeperTeamId` | `string (uuid-v4)` | ✅ | Equipe du gardien |
| `relatedShotEventId` | `string (uuid-v4)` | ❌ | Reference au tir correspondant |

## Regles metier

- Peut etre relie a un `SHOT_ATTEMPTED` pour reconstruire l'action.
- Contribue aux stats d'arret du gardien.
