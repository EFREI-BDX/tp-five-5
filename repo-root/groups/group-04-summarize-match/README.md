# summarize-match

Service de resume de match dans une architecture DDD hexagonale.

## Documentation canonique

- `value-object/*.md` - fiches par Value Object (PlayerId, TeamId)
- `aggregate/*.md` - fiches par agregat du coeur domaine
- `entity/*.md` - fiches par entite ou entree de read model identifiee

## Ce que fait le service

- Consomme un flux d'events de match venant du contexte amont `record-match`.
- Reconstitue une timeline coherente.
- Calcule les scores et les statistiques derives.
- Signale les anomalies metier quand la timeline ou le score final ne sont pas coherents.

## Endpoints HTTP

- `POST /events` - reception et validation d'un event de match.
- `GET /matches/{matchId}/summary` - lecture du resume calcule du match (score, buts, cartons, remplacements).
- `GET /matches/{matchId}/teams/{teamId}/stats` - lecture des statistiques calculees pour une equipe.
- `GET /matches/{matchId}/players/{playerId}/stats` - lecture des statistiques calculees pour un joueur.
- `GET /health` - controle technique.

## Stockage et stats SQL

- Les events acceptes par `POST /events` sont persistes dans `match_events`.
- Les stats equipes et joueurs sont materialisees dans Postgres par la fonction stockee `apply_match_event_stats(event_json JSONB)`.
- Les tables `match_team_stats` et `match_player_stats` servent directement les endpoints `/stats`.
- `match_goal_index` permet d'annuler proprement un but avec `GOAL_CANCELLED`.
- `match_player_registry` conserve l'association joueur/equipe et le nombre de joueurs utilises.

## Ce que le service ne fait pas encore

- Publication d'events sortants reels : le port `DomainEventPublisher` est cable, mais l'adapter est NoOp. Remplacer par un adapter Kafka/AMQP pour notifier les contextes `reporting`, `ranking`, `statistics`.
