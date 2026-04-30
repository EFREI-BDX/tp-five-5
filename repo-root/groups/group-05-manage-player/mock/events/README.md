# Event mocks

Payloads de test pour les 5 events entrants du service `manage-player`.

Variables utiles :

- `BASE_URL=http://localhost:8080`
- `API_KEY=dev-api-key`

Exemples :

```bash
curl -X POST "$BASE_URL/events/teams/name-updated" -H "Content-Type: application/json" -H "X-API-KEY: $API_KEY" -d @team-name-updated.valid.json
curl -X POST "$BASE_URL/events/teams/deleted" -H "Content-Type: application/json" -H "X-API-KEY: $API_KEY" -d @team-deleted.valid.json
curl -X POST "$BASE_URL/events/teams/player-joined" -H "Content-Type: application/json" -H "X-API-KEY: $API_KEY" -d @player-joined-team.valid.json
curl -X POST "$BASE_URL/events/teams/player-left" -H "Content-Type: application/json" -H "X-API-KEY: $API_KEY" -d @player-left-team.valid.json
curl -X POST "$BASE_URL/events/matches/player" -H "Content-Type: application/json" -H "X-API-KEY: $API_KEY" -d @match-player-event.valid.json
```

Les UUID de team utilises ici sont seedes dans `sql/player-create.sql`.
