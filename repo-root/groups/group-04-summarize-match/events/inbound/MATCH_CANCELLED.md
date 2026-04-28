# MATCH_CANCELLED

## Role

Event d'annulation du match avant obtention d'un resultat exploitable.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_CANCELLED",
  "occurredAt": "2024-11-15T19:00:00.000Z",
  "matchTime": { "minute": 0, "second": 0, "period": "FIRST_HALF" },
  "payload": { "reason": "PITCH_UNAVAILABLE" }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `reason` | `string (enum)` | ✅ | `PITCH_UNAVAILABLE`, `WEATHER`, `ADMINISTRATIVE`, `OTHER` |

## Regles metier

- Aucun resultat metier exploitable ne doit etre derive apres cet event.
- Il met fin au cycle de vie du match avec un statut annule.
