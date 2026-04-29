# MATCH_STARTED

## Role

Premier event de la timeline. Il fixe les equipes de depart et la duree theorique du match.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_STARTED",
  "occurredAt": "2024-11-15T20:00:00.000Z",
  "matchTime": { "minute": 0, "second": 0, "period": "FIRST_HALF" },
  "payload": {
    "homeTeam": {
      "teamId": "uuid-v4",
      "startingPlayers": [
        { "playerId": "uuid-v4", "isGoalkeeper": true },
        { "playerId": "uuid-v4", "isGoalkeeper": false },
        { "playerId": "uuid-v4", "isGoalkeeper": false },
        { "playerId": "uuid-v4", "isGoalkeeper": false },
        { "playerId": "uuid-v4", "isGoalkeeper": false }
      ]
    },
    "awayTeam": {
      "teamId": "uuid-v4",
      "startingPlayers": [
        { "playerId": "uuid-v4", "isGoalkeeper": true },
        { "playerId": "uuid-v4", "isGoalkeeper": false },
        { "playerId": "uuid-v4", "isGoalkeeper": false },
        { "playerId": "uuid-v4", "isGoalkeeper": false },
        { "playerId": "uuid-v4", "isGoalkeeper": false }
      ]
    },
    "scheduledDurationMinutes": 40
  }
}
```


faire sauter le isGoalKeeper 

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `homeTeam.teamId` | `string (uuid-v4)` | ✅ | Equipe domicile |
| `homeTeam.startingPlayers` | `array[5]` | ✅ | Exactement 5 titulaires |
| `startingPlayers[].playerId` | `string (uuid-v4)` | ✅ | Identifiant du joueur |
| `startingPlayers[].isGoalkeeper` | `boolean` | ✅ | `true` pour le gardien |
| `awayTeam` | `object` | ✅ | Meme structure que `homeTeam` |
| `scheduledDurationMinutes` | `integer > 0` | ✅ | Duree theorique du match |

## Regles metier

- Doit etre le premier event de la timeline.
- Chaque equipe doit avoir exactement un gardien titulaire.
- `startingPlayers` doit contenir exactement 5 joueurs.
