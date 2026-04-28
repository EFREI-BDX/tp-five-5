# MATCH_FINISHED

## Role

Event de cloture du match. Il confirme le score final.

## Exemple

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_FINISHED",
  "occurredAt": "2024-11-15T21:00:00.000Z",
  "matchTime": { "minute": 40, "second": 0, "period": "SECOND_HALF" },
  "payload": { "finalScore": { "home": 3, "away": 2 } }
}
```

## Regles payload

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `finalScore.home` | `integer >= 0` | ✅ | Buts de l'equipe domicile |
| `finalScore.away` | `integer >= 0` | ✅ | Buts de l'equipe exterieure |

## Regles metier

- Doit etre le dernier event de jeu.
- Le score final doit pouvoir etre recalcule depuis la timeline.
- Toute divergence entre score calcule et score fourni doit remonter comme anomalie.
