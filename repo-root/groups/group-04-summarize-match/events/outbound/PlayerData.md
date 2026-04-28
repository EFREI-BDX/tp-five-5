# PlayerData (outbound)

## Role

Event produit par `resume-match` : résumé des statistiques d'un joueur à la fin du match.

## Structure

Le payload suit le schema `tests/schemas/player-data.schema.json`.

## Exemple

```json
{
  "playerId": "uuid-v4",
  "goals": 2,
  "assists": 1,
  "saves": 0,
  "result": "Win",
  "bestScorer": false,
  "bestAssistsProvider": false,
  "MVP": false,
  "playTime": 35
}
```

## Remarques

- Le schema et les fixtures ont ete normalisés pour utiliser `playerId` (correction de la typo).
