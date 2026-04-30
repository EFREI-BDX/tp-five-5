# API HTTP

## Base URL Locale

```text
http://localhost:8080
```

Toutes les routes métier nécessitent :

```http
X-API-KEY: dev-api-key
```

## Routes Publiques

### `GET /health`

Vérifie que l'application répond.

Réponse :

```json
{
  "status": "UP"
}
```

## Routes Players

### `GET /players`

Retourne tous les joueurs.

Exemple :

```bash
curl http://localhost:8080/players \
  -H "X-API-KEY: dev-api-key"
```

Réponse `200 OK` :

```json
[
  {
    "id": "11111111-1111-4111-8111-111111111111",
    "firstName": "Lionel",
    "lastName": "Messi",
    "email": "lionel.messi@example.com",
    "phone": "+33610000001",
    "birthDate": "24/06/1987",
    "gender": "homme",
    "height": 170.0,
    "status": "actif",
    "statistics": {
      "matchesPlayed": 60,
      "goalsScored": 42,
      "assists": 28,
      "wins": 39,
      "losses": 10,
      "draws": 11,
      "mvps": 18
    },
    "teamIds": [],
    "createdAt": "2026-04-27T08:00:00Z",
    "updatedAt": "2026-04-27T08:00:00Z"
  }
]
```

### `POST /players`

Crée un joueur.

Exemple :

```bash
curl -X POST http://localhost:8080/players \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-api-key" \
  -d '{
    "firstName": "Jean",
    "lastName": "Dupont",
    "email": "jean.dupont@example.com",
    "phone": "+33612345678",
    "birthDate": "15/06/1995",
    "gender": "homme",
    "height": 178.5
  }'
```

Réponse `201 Created` :

```json
{
  "id": "uuid",
  "status": "actif",
  "createdAt": "2026-04-30T10:00:00Z"
}
```

### `GET /players/{id}`

Retourne un joueur complet.

Exemple :

```bash
curl http://localhost:8080/players/<player-id> \
  -H "X-API-KEY: dev-api-key"
```

### `PUT /players/{id}`

Met à jour partiellement un joueur.

Les champs absents ou `null` ne remplacent pas les valeurs existantes.

Exemple :

```bash
curl -X PUT http://localhost:8080/players/<player-id> \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-api-key" \
  -d '{
    "phone": "+33698765432"
  }'
```

### `DELETE /players/{id}`

Supprime logiquement un joueur.

La suppression ne retire pas physiquement le joueur du repository. Le statut passe à :

```text
supprimé
```

### `POST /players/{id}/statistics`

Met à jour les statistiques du joueur.

Exemple :

```bash
curl -X POST http://localhost:8080/players/<player-id>/statistics \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-api-key" \
  -d '{
    "matchesPlayed": 10,
    "goalsScored": 4,
    "assists": 2,
    "wins": 6,
    "losses": 2,
    "draws": 2,
    "mvps": 1
  }'
```

Règles :

- toutes les statistiques doivent être renseignées ;
- les valeurs doivent être supérieures ou égales à 0 ;
- `wins + losses + draws` ne peut pas être supérieur à `matchesPlayed` ;
- `mvps` ne peut pas être supérieur à `matchesPlayed`.

## Routes Inbound Events

### `POST /events/teams/player-joined`

Associe un joueur à une équipe.

Payload :

```json
{
  "playerId": "uuid",
  "teamId": "uuid"
}
```

Réponse :

```http
202 Accepted
```

Code :

```text
TeamEventsInboundController -> PlayerSyncService -> PlayerRepository.playerJoinTeam
```

### `POST /events/teams/player-left`

Retire une équipe de la liste des équipes associées au joueur.

Payload :

```json
{
  "playerId": "uuid",
  "teamId": "uuid"
}
```

Réponse :

```http
202 Accepted
```

Code :

```text
TeamEventsInboundController -> PlayerSyncService -> PlayerRepository.playerLeaveTeam
```

## Événements Sortants

Les contrats `PlayerCreated`, `PlayerDeleted` et `PlayerNameUpdated` sont documentés dans `../event/out`, mais le code applicatif actuel ne les publie pas encore sur un bus d'événements. Pour la présentation, il faut les présenter comme des événements prévus et contractualisés, pas comme une fonctionnalité déjà branchée.

## Routes Admin

Les routes admin sont exposées sous :

```text
/admin/players
```

Routes :

- `GET /admin/players/count` : retourne le nombre de joueurs persistés ;
- `DELETE /admin/players` : réinitialise les joueurs via la procédure `playerReset`.

Ces routes vérifient la propriété `app.admin.enabled`. Elle vaut `false` par défaut ; si elle n'est pas activée, les endpoints retournent `404`.
