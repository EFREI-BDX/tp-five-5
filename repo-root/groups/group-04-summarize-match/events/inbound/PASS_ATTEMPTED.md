# PASS_ATTEMPTED

## Role

Event de tentative de passe.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "PASS_ATTEMPTED",
  "occurredAt": "2024-11-15T20:03:05.000Z",
  "matchTime": { "minute": 3, "second": 5, "period": "FIRST_HALF" },
  "payload": {
    "passerId": "uuid-v4",
    "teamId": "uuid-v4",
    "receiverId": "uuid-v4",
    "succeeded": true
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `passerId` | `string (uuid-v4)` | ✅ | Joueur passeur |
| `teamId` | `string (uuid-v4)` | ✅ | Equipe du passeur |
| `receiverId` | `string (uuid-v4)` | ❌ | Receveur si identifiable |
| `succeeded` | `boolean` | ✅ | `true` si passe reussie |

## Regles metier

- Ce document couvre aussi le cas semantique `PASS_SUCCEEDED`.
- Il n'y a pas de fichier de transport separé pour `PASS_SUCCEEDED`; la reussite est porte par `succeeded=true`.
