# SHOT_ATTEMPTED

## Role

Event de tentative de tir, cadre ou non.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "SHOT_ATTEMPTED",
  "occurredAt": "2024-11-15T20:05:12.000Z",
  "matchTime": { "minute": 5, "second": 12, "period": "FIRST_HALF" },
  "payload": {
    "shooterId": "uuid-v4",
    "teamId": "uuid-v4",
    "onTarget": true,
    "outcome": "SAVED"
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `shooterId` | `string (uuid-v4)` | ✅ | Joueur tireur |
| `teamId` | `string (uuid-v4)` | ✅ | Equipe du tireur |
| `onTarget` | `boolean` | ✅ | `true` si tir cadre |
| `outcome` | `string (enum)` | ✅ | `GOAL`, `SAVED`, `BLOCKED`, `WIDE`, `POST` |

## Regles metier

- `outcome=GOAL` doit toujours etre accompagne d'un `GOAL_SCORED` distinct.
- `onTarget=true` correspond a un tir cadre, independamment du resultat final.
