# MatchCancelled

**Resume metier**

Evenement outbound d'annulation de match. Aucun resultat officiel n'est conserve.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_CANCELLED",
  "occurredAt": "2024-11-15T20:00:00.000Z",
  "matchTime": {
    "minute": 0,
    "second": 0,
    "period": "FIRST_HALF"
  },
  "payload": {}
}
```

**Champs principaux**

- `type` - valeur fixe `MATCH_CANCELLED`
