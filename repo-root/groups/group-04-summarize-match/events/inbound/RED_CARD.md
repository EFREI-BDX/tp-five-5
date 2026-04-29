# RED_CARD

## Role

Event d'expulsion d'un joueur.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "RED_CARD",
  "occurredAt": "2024-11-15T20:22:10.000Z",
  "matchTime": { "minute": 22, "second": 10, "period": "SECOND_HALF" },
  "payload": {
    "playerId": "uuid-v4",
    "teamId": "uuid-v4",
    "isDoubleYellow": false,
    "relatedFoulEventId": "uuid-v4"
  }
}
```

isdoubleYellow a supprimé et relatedFoulEventId

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `playerId` | `string (uuid-v4)` | ✅ | Joueur expulse |
| `teamId` | `string (uuid-v4)` | ✅ | Equipe du joueur |
| `isDoubleYellow` | `boolean` | ✅ | `true` si second jaune |
| `relatedFoulEventId` | `string (uuid-v4)` | ❌ | Reference au declencheur |

## Regles metier

- A partir de cet event, les actions de jeu de ce joueur deviennent invalides pour le contexte `resume-match`.
- C'est un point de rupture de la timeline metier pour le joueur concerne.
