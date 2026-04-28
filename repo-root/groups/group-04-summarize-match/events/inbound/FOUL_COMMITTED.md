# FOUL_COMMITTED

## Role

Event de faute sifflée.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "FOUL_COMMITTED",
  "occurredAt": "2024-11-15T20:09:00.000Z",
  "matchTime": { "minute": 9, "second": 0, "period": "FIRST_HALF" },
  "payload": {
    "playerId": "uuid-v4",
    "teamId": "uuid-v4",
    "againstPlayerId": "uuid-v4"
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `playerId` | `string (uuid-v4)` | ✅ | Joueur fautif |
| `teamId` | `string (uuid-v4)` | ✅ | Equipe du fautif |
| `againstPlayerId` | `string (uuid-v4)` | ❌ | Joueur victime |

## Regles metier

- Sert de base de correlation pour les cartons.
- Alimente les statistiques de fautes.
