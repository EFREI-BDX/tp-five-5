# Routes et Événements

## Vue d'Ensemble

Le service expose trois familles de routes :

- routes publiques de supervision ;
- routes métier `players`, protégées par `X-API-KEY` ;
- routes d'intégration événementielle et routes admin, également protégées par `X-API-KEY`.

```text
Requête HTTP
  -> ApiKeyFilter
  -> Controller
  -> Service
  -> Repository
  -> Procédure stockée MariaDB
```

## Routes HTTP

| Méthode | Route | Rôle | Classe d'entrée | Service appelé | Persistance |
|---|---|---|---|---|---|
| `GET` | `/health` | Vérifier que l'application répond | `HealthController` | aucun | aucune |
| `GET` | `/players` | Lister tous les joueurs | `PlayerController.findAll` | `PlayerService.findAll` | `playerGetAll()` |
| `POST` | `/players` | Créer un joueur | `PlayerController.create` | `PlayerService.create` | `playerCreate(...)` |
| `GET` | `/players/{id}` | Consulter un joueur | `PlayerController.findById` | `PlayerService.findById` | `playerGetById(?)` |
| `PUT` | `/players/{id}` | Modifier un joueur | `PlayerController.update` | `PlayerService.update` | `playerUpdate(...)` |
| `DELETE` | `/players/{id}` | Supprimer logiquement un joueur | `PlayerController.delete` | `PlayerService.delete` | `playerDelete(?)` |
| `POST` | `/players/{id}/statistics` | Remplacer les statistiques cumulées | `PlayerController.updateStatistics` | `PlayerService.updateStatistics` | `playerStatisticsUpdate(...)` |
| `POST` | `/events/teams/player-joined` | Associer un joueur à une équipe | `TeamEventsInboundController.playerJoinedTeam` | `PlayerSyncService.playerJoinedTeam` | `playerJoinTeam(...)` |
| `POST` | `/events/teams/player-left` | Retirer un joueur d'une équipe | `TeamEventsInboundController.playerLeftTeam` | `PlayerSyncService.playerLeftTeam` | `playerLeaveTeam(...)` |
| `GET` | `/admin/players/count` | Compter les joueurs | `PlayerAdminController.count` | `IPlayerAdminService.count` | `playerCount()` |
| `DELETE` | `/admin/players` | Réinitialiser les joueurs | `PlayerAdminController.reset` | `IPlayerAdminService.reset` | `playerReset(?)` |

## Détail des Routes Métier

### `GET /players`

Retourne la liste des joueurs triée côté base par nom puis prénom.

Flux :

```text
PlayerController.findAll
  -> PlayerService.findAll
  -> PlayerRepository.findAll
  -> CALL fiveplayer.playerGetAll()
```

Réponse : `200 OK` avec un tableau de `PlayerDto`.

### `POST /players`

Crée un joueur actif avec des statistiques initialisées à zéro.

Flux :

```text
PlayerController.create
  -> validation Jakarta sur CreatePlayerRequest
  -> PlayerService.create
  -> validations métier complémentaires
  -> PlayerMapper.fromCreateRequest
  -> PlayerRepository.create
  -> CALL fiveplayer.playerCreate(...)
```

Règles principales :

- prénom et nom obligatoires, maximum 100 caractères ;
- email obligatoire et unique ;
- téléphone français accepté au format `+33...` ou `0...` ;
- date de naissance au format `dd/MM/yyyy`, non future ;
- genre parmi `homme`, `femme`, `non binaire`, `non spécifié` ;
- taille strictement positive.

Réponse : `201 Created` avec `id`, `status` et `createdAt`.

### `GET /players/{id}`

Retourne le profil complet d'un joueur.

Flux :

```text
PlayerController.findById
  -> PlayerService.findById
  -> PlayerRepository.findById
  -> CALL fiveplayer.playerGetById(?)
```

Réponse : `200 OK` avec `PlayerDto`, ou `404 Not Found` si le joueur n'existe pas.

### `PUT /players/{id}`

Met à jour uniquement les champs fournis. Les champs absents ou `null` conservent leur valeur actuelle.

Flux :

```text
PlayerController.update
  -> validation Jakarta sur UpdatePlayerRequest
  -> PlayerService.update
  -> requireActivePlayer
  -> PlayerMapper.merge
  -> PlayerRepository.update
  -> CALL fiveplayer.playerUpdate(...)
```

Règles principales :

- un joueur supprimé ne peut plus être modifié ;
- les champs fournis respectent les mêmes règles que la création ;
- la base refuse une mise à jour sans champ utile ;
- l'email reste unique.

Réponse : `200 OK` avec `id` et `updatedAt`.

### `DELETE /players/{id}`

Supprime logiquement un joueur en passant son statut à `supprimé`.

Flux :

```text
PlayerController.delete
  -> PlayerService.delete
  -> PlayerRepository.delete
  -> CALL fiveplayer.playerDelete(?)
```

Réponse : `200 OK` avec `id`, `status` et `updatedAt`.

### `POST /players/{id}/statistics`

Remplace les statistiques cumulées d'un joueur.

Flux :

```text
PlayerController.updateStatistics
  -> validation Jakarta sur UpdatePlayerStatisticsRequest
  -> PlayerService.updateStatistics
  -> validateStatistics
  -> PlayerRepository.updateStatistics
  -> CALL fiveplayer.playerStatisticsUpdate(...)
```

Statistiques attendues :

- `matchesPlayed` ;
- `goalsScored` ;
- `assists` ;
- `wins` ;
- `losses` ;
- `draws` ;
- `mvps`.

Règles principales :

- toutes les valeurs sont obligatoires ;
- chaque valeur est positive ou nulle ;
- `wins + losses + draws <= matchesPlayed` ;
- `mvps <= matchesPlayed` ;
- un joueur supprimé ne peut plus recevoir de nouvelles statistiques.

Réponse : `200 OK` avec `id`, `statistics` et `updatedAt`.

## Événements Entrants

Les événements entrants sont représentés par des endpoints HTTP. Cela permet de tester l'intégration sans bus de messages.

### `PlayerJoinedTeam`

Route technique :

```http
POST /events/teams/player-joined
```

Payload :

```json
{
  "playerId": "11111111-1111-4111-8111-111111111111",
  "teamId": "550e8400-e29b-41d4-a716-446655440000"
}
```

Flux :

```text
TeamEventsInboundController.playerJoinedTeam
  -> PlayerSyncService.playerJoinedTeam
  -> PlayerRepository.findById
  -> PlayerRepository.addTeam
  -> CALL fiveplayer.playerJoinTeam(...)
```

Effet : ajoute une ligne dans `player_team` si le joueur et l'équipe existent.

### `PlayerLeftTeam`

Route technique :

```http
POST /events/teams/player-left
```

Payload :

```json
{
  "playerId": "11111111-1111-4111-8111-111111111111",
  "teamId": "550e8400-e29b-41d4-a716-446655440000"
}
```

Flux :

```text
TeamEventsInboundController.playerLeftTeam
  -> PlayerSyncService.playerLeftTeam
  -> PlayerRepository.findById
  -> PlayerRepository.removeTeam
  -> CALL fiveplayer.playerLeaveTeam(...)
```

Effet : supprime l'association `player_team` existante.

### `MatchPlayerEvent`

Contrat documenté côté domaine : `../event/in/match/MatchPlayerEvent.md`.

État actuel de l'application : aucune route dédiée ne consomme directement cet événement. Le comportement équivalent est porté par `POST /players/{id}/statistics`, qui reçoit les mêmes statistiques hors `playerId` placé dans le path.

## Événements Sortants

Les événements sortants sont documentés comme contrats métier, mais ils ne sont pas encore publiés par le code applicatif.

| Événement | Déclencheur métier | Fichier de contrat | État actuel |
|---|---|---|---|
| `PlayerCreated` | création d'un joueur | `event/out/PlayerCreated.md` | documenté, non publié |
| `PlayerDeleted` | suppression logique | `event/out/PlayerDeleted.md` | documenté, non publié |
| `PlayerNameUpdated` | modification du prénom ou du nom | `event/out/PlayerNameUpdated.md` | documenté, non publié |

Pour une version connectée à un bus de messages, la publication devrait être déclenchée dans `PlayerService` après succès repository, idéalement via un port applicatif dédié pour ne pas coupler la logique métier à une technologie de messaging.
