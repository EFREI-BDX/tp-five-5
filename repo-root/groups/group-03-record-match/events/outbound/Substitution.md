# Substitution

**Resume metier**

Evenement outbound de remplacement de joueur.

**JSON attendu**

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "SUBSTITUTION",
  "occurredAt": "2024-11-15T20:18:00.000Z",
  "matchTime": {
    "minute": 18,
    "second": 0,
    "period": "SECOND_HALF"
  },
  "payload": {
    "playerOutId": "uuid-v4",
    "playerInId": "uuid-v4"
  }
}
```

**Champs principaux**

- `payload.teamId` - equipe effectuant le changement
- `payload.playerOutId` - joueur sortant
- `payload.playerInId` - joueur entrant

**Contraintes**

- `playerOutId` ne peut pas etre un joueur deja expulse
- `playerInId` doit etre inscrit sur la feuille de match mais pas encore entre