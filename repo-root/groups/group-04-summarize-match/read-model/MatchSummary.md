# Read Model — MatchSummary

## Role

`MatchSummary` expose l'etat courant d'un match reconstruit depuis la timeline d'events.

## Route HTTP

```txt
GET /matches/{matchId}/summary
```

## Reponse

Schema JSON : `tests/schemas/match-summary.schema.json`

```json
{
  "matchId": "11111111-2222-3333-4444-555555555555",
  "status": "IN_PROGRESS",
  "homeTeamId": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
  "awayTeamId": "ffffffff-eeee-dddd-cccc-bbbbbbbbbbbb",
  "score": { "home": 1, "away": 0 },
  "goals": [],
  "cards": [],
  "substitutions": []
}
```

## Statuts HTTP

- `200 OK` — resume trouve.
- `400 Bad Request` — `matchId` n'est pas un UUID valide.
- `404 Not Found` — aucun match connu pour cet identifiant.
- `422 Unprocessable Entity` — une regle domaine est violee pendant le replay.
- `500 Internal Server Error` — erreur de repository.

## Construction

Calcul effectue par `MatchAggregate::to_summary(match_id)`.

Fixture de contrat : `tests/fixtures/match-summary.valid.json`

## References de code

- Type: `summarize-match/src/domain/summary.rs`
- Calcul: `summarize-match/src/domain/aggregate.rs`
- Port query: `summarize-match/src/application/query_service.rs`
- Handler HTTP: `summarize-match/src/infrastructure/inbound/http/query_handlers.rs`
