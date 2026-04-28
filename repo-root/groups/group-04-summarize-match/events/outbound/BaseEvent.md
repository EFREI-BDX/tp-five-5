# BaseEvent (outbound)

## Role

Enveloppe commune pour les events produits par `resume-match`.

## Structure

Les events produits respectent la meme enveloppe que les events entrants :

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "PlayerData",
  "occurredAt": "2024-11-15T21:00:00.000Z",
  "matchTime": {
    "minute": 40,
    "second": 0,
    "period": "SECOND_HALF"
  },
  "payload": {}
}
```

## Portée hexagonale

Cette enveloppe constitue le contrat du port secondaire (driven). Le domaine publie des événements métier sous cette enveloppe ; l'adapter d'émission (infrastructure) sérialise les objets métier dans `payload` sans exposer les détails internes du domaine.

## Remarques

- Les règles de cohérence métier (par ex. score final calculable depuis la timeline) restent une responsabilité du domaine ; l'enveloppe ne transporte que le résultat.
- Versionnez les sorties si la structure de `payload` change (ex : `PlayerData.v2`).
