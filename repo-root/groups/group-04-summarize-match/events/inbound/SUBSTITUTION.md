# SUBSTITUTION

## Role

Event de remplacement de joueur.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "SUBSTITUTION",
  "occurredAt": "2024-11-15T20:18:00.000Z",
  "matchTime": { "minute": 18, "second": 0, "period": "SECOND_HALF" },
  "payload": {
    "teamId": "uuid-v4",
    "playerOutId": "uuid-v4",
    "playerInId": "uuid-v4"
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `teamId` | `string (uuid-v4)` | ✅ | Equipe effectuant le changement |
| `playerOutId` | `string (uuid-v4)` | ✅ | Joueur qui sort |
| `playerInId` | `string (uuid-v4)` | ✅ | Joueur qui entre |

## Regles metier

- `playerOutId` ne peut pas etre deja expulse.
- `playerInId` doit etre inscrit sur la feuille de match mais encore non entre.
