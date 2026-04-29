# Read Model — PlayerMatchStats

## Role

`PlayerMatchStats` expose les statistiques calculees pour chaque joueur d'un match.

## Route HTTP

```txt
GET /matches/{matchId}/players/stats
```

## Reponse

Schema JSON : `tests/schemas/player-match-stats.schema.json`

La route retourne un tableau, un element par joueur connu du match.

```json
[
  {
    "playerId": "00000000-0000-0000-0000-000000000002",
    "goals": 1,
    "assists": 0,
    "saves": 0,
    "result": "Win",
    "bestScorer": true,
    "bestAssistsProvider": false,
    "MVP": true,
    "playTime": 2400
  }
]
```

## Champs

| Champ | Type | Description |
|---|---|---|
| `playerId` | `PlayerId` | Identifiant du joueur. |
| `goals` | `u32` | Nombre de buts marques par le joueur. |
| `assists` | `u32` | Nombre de passes decisives. |
| `saves` | `u32` | Nombre d'arrets. |
| `result` | `MatchResult` | Resultat du match du point de vue du joueur (`Win`, `Loss`, `Draw`). |
| `bestScorer` | `bool` | `true` si le joueur partage ou detient le meilleur total de buts. |
| `bestAssistsProvider` | `bool` | `true` si le joueur partage ou detient le meilleur total d'assists. |
| `MVP` | `bool` | `true` pour le joueur retenu comme MVP selon le score `goals + assists + saves`. |
| `playTime` | `u32` | Temps de jeu en secondes. |

## Statuts HTTP

- `200 OK` — statistiques trouvees.
- `400 Bad Request` — `matchId` n'est pas un UUID valide.
- `404 Not Found` — aucun match connu pour cet identifiant.
- `422 Unprocessable Entity` — une regle domaine est violee pendant le replay.
- `500 Internal Server Error` — erreur de repository.

## Construction

Calcul effectue par `MatchAggregate::to_player_stats()`.

`PlayerMatchStats` est un contrat de lecture HTTP, pas un event outbound.

Fixture de contrat : `tests/fixtures/player-match-stats.valid.json`

## References de code

- Type: `summarize-match/src/domain/summary.rs`
- Calcul: `summarize-match/src/domain/aggregate.rs`
- Port query: `summarize-match/src/application/query_service.rs`
- Handler HTTP: `summarize-match/src/infrastructure/inbound/http/query_handlers.rs`
