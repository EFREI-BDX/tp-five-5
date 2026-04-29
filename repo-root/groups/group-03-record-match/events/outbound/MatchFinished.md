# MatchFinished

**Resume metier**

Evenement outbound de fin de match. Il clot la timeline et porte le score final calcule.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_FINISHED",
  "occurredAt": "2024-11-15T21:00:00.000Z",
  "matchTime": {
    "minute": 40,
    "second": 0,
    "period": "SECOND_HALF"
  },
  "payload": {}
}
```

**Champs principaux**

- `type` - valeur fixe `MATCH_FINISHED`

**Contrainte**

- le score final doit correspondre au cumul des `GOAL_SCORED` valides
