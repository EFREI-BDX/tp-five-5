# YELLOW_CARD

## Role

Event d'attribution d'un carton jaune.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "YELLOW_CARD",
  "occurredAt": "2024-11-15T20:09:05.000Z",
  "matchTime": { "minute": 9, "second": 5, "period": "FIRST_HALF" },
  "payload": {
    "playerId": "uuid-v4",
    "teamId": "uuid-v4",
    "relatedFoulEventId": "uuid-v4",
    "cardNumber": 1
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `playerId` | `string (uuid-v4)` | ✅ | Joueur sanctionne |
| `teamId` | `string (uuid-v4)` | ✅ | Equipe du joueur |
| `relatedFoulEventId` | `string (uuid-v4)` | ❌ | Reference a la faute associee |
| `cardNumber` | `integer [1-2]` | ✅ | `1` pour premier jaune, `2` pour second jaune |

## Regles metier

- `cardNumber=2` precede un `RED_CARD` par second jaune.
- Peut etre relie a une faute pour l'explicabilite du resume.
