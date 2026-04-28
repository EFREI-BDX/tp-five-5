# MATCH_FORFEITED

## Role

Event de forfait d'une equipe.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_FORFEITED",
  "occurredAt": "2024-11-15T20:12:30.000Z",
  "matchTime": { "minute": 12, "second": 30, "period": "FIRST_HALF" },
  "payload": {
    "forfeitingTeamId": "uuid-v4",
    "reason": "TEAM_ABSENT",
    "administrativeScore": { "home": 3, "away": 0 },
    "statsPolicy": "DISCARDED"
  }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `forfeitingTeamId` | `string (uuid-v4)` | ✅ | Equipe declaree forfaitaire |
| `reason` | `string (enum)` | ✅ | `TEAM_ABSENT`, `PLAYER_SHORTAGE`, `WALKOUT`, `OTHER` |
| `administrativeScore.home` | `integer >= 0` | ✅ | Score administratif domicile |
| `administrativeScore.away` | `integer >= 0` | ✅ | Score administratif exterieur |
| `statsPolicy` | `string (enum)` | ✅ | `DISCARDED`, `KEPT`, `NON_OFFICIAL` |

## Regles metier

- Les stats partielles sont conservees ou jetees selon `statsPolicy`.
- Le forfait met fin au match du point de vue metier.
