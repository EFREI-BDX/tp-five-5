# GOAL_SCORED

## Role

Event de but valide.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "GOAL_SCORED",
  "occurredAt": "2024-11-15T20:07:43.000Z",
  "matchTime": { "minute": 7, "second": 43, "period": "FIRST_HALF" },
  "payload": {
    "scoringTeamId": "uuid-v4",
    "scorerId": "uuid-v4",
    "assistId": "uuid-v4",
    "isOwnGoal": false
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `scoringTeamId` | `string (uuid-v4)` | ✅ | Equipe creditée du but |
| `scorerId` | `string (uuid-v4)` | ✅ | Auteur du but ou du CSC |
| `assistId` | `string (uuid-v4)` | ❌ | Passeur decisif si applicable |
| `isOwnGoal` | `boolean` | ✅ | `true` si but contre son camp |

## Regles metier

- En cas de CSC, `scorerId` identifie le joueur ayant marque contre son camp.
- `scoringTeamId` peut differer de l'equipe du `scorerId` si `isOwnGoal=true`.
