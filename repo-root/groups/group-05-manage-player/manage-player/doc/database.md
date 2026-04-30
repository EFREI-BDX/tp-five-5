# Base de Données

## Vue d'Ensemble

La base utilisée par `manage-player` est MariaDB. Elle est initialisée par les scripts du dossier :

```text
groups/group-05-manage-player/sql
```

Les scripts sont montés automatiquement par `docker-compose.yml` :

```text
01-player-create.sql
02-views.sql
03-stored-procedures.sql
```

Le service Java accède à la base via `JdbcTemplate`, dans :

```text
repository/PlayerRepository.java
```

## Schéma

Nom du schéma :

```text
fiveplayer
```

Utilisateur applicatif local :

```text
jad_efrei_five_2526
```

## Tables

### `player`

Table principale des joueurs.

| Colonne | Type | Rôle |
|---|---|---|
| `id` | `char(36)` | Identifiant UUID du joueur |
| `firstName` | `varchar(100)` | Prénom |
| `lastName` | `varchar(100)` | Nom |
| `email` | `varchar(255)` | Email |
| `phone` | `varchar(20)` | Téléphone |
| `gender` | `enum` | Genre déclaré |
| `birthDate` | `date` | Date de naissance |
| `height` | `decimal(5,2)` | Taille en centimètres |
| `status` | `enum` | `actif`, `inactif`, `supprimé` |
| `createdAt` | `datetime` | Date de création |
| `updatedAt` | `datetime` | Date de dernière modification |

Contraintes :

- `height > 0` ;
- `updatedAt >= createdAt`.

### `player_statistics`

Table des statistiques cumulées d'un joueur.

| Colonne | Type | Rôle |
|---|---|---|
| `idPlayer` | `char(36)` | Clé primaire et référence vers `player.id` |
| `matchesPlayed` | `int unsigned` | Matchs joués |
| `goalsScored` | `int unsigned` | Buts marqués |
| `assists` | `int unsigned` | Passes décisives |
| `wins` | `int unsigned` | Victoires |
| `losses` | `int unsigned` | Défaites |
| `draws` | `int unsigned` | Matchs nuls |
| `mvps` | `int unsigned` | Nombre de titres MVP |

Contraintes :

- `idPlayer` référence `player(id)` ;
- `wins + losses + draws <= matchesPlayed` ;
- `mvps <= matchesPlayed`.

### `team`

Table minimale des équipes utilisée pour valider les associations joueur-équipe.

| Colonne | Type | Rôle |
|---|---|---|
| `id` | `char(36)` | Identifiant UUID de l'équipe |
| `name` | `varchar(100)` | Nom de l'équipe |

### `player_team`

Table d'association entre joueurs et équipes.

| Colonne | Type | Rôle |
|---|---|---|
| `idPlayer` | `char(36)` | Référence vers `player.id` |
| `idTeam` | `char(36)` | Référence vers `team.id` |

Contraintes :

- clé primaire composée `(idPlayer, idTeam)` ;
- `idPlayer` référence `player(id)` ;
- `idTeam` référence `team(id)`.

## Relations

```text
player 1 ── 1 player_statistics
player 1 ── n player_team n ── 1 team
```

## Vues

Les vues servent à fournir au code Java un format déjà adapté à l'API.

### `TeamView`

Expose :

- `id` ;
- `name`.

Utilisée par les procédures de lecture liées aux équipes.

### `PlayerView`

Expose les données profil du joueur avec des formats API :

- `birthDate` est formaté en `dd/MM/yyyy` ;
- `createdAt` et `updatedAt` sont formatés en ISO-like `yyyy-MM-ddTHH:mm:ssZ`.

Colonnes exposées :

- `id`, `firstName`, `lastName`, `email`, `phone`, `gender`, `birthDate`, `height`, `status`, `createdAt`, `updatedAt`.

### `PlayerStatisticsView`

Expose les statistiques cumulées :

- `idPlayer` ;
- `matchesPlayed` ;
- `goalsScored` ;
- `assists` ;
- `wins` ;
- `losses` ;
- `draws` ;
- `mvps`.

### `PlayerTeamView`

Expose les associations :

- `idPlayer` ;
- `idTeam`.

`playerGetAll` et `playerGetById` utilisent cette vue pour construire `teamIds`.

## Procédures de Validation

Ces procédures factorisent les règles de validation SQL :

| Procédure | Rôle |
|---|---|
| `idCheck` | Vérifie un identifiant non vide |
| `playerIdCheck` | Vérifie un UUID joueur |
| `teamNameCheck` | Vérifie un nom d'équipe |
| `playerFirstNameCheck` | Vérifie le prénom |
| `playerLastNameCheck` | Vérifie le nom |
| `playerEmailCheck` | Vérifie l'email |
| `playerPhoneCheck` | Vérifie le téléphone |
| `playerGenderCheck` | Vérifie le genre |
| `playerBirthDateCheck` | Vérifie la date de naissance |
| `playerHeightCheck` | Vérifie la taille |
| `playerStatisticsCheck` | Vérifie la cohérence des statistiques |

## Procédures Métier Joueur

| Procédure | Appelée par | Rôle |
|---|---|---|
| `playerCreate` | `PlayerRepository.create` | Crée un joueur actif et initialise ses statistiques à zéro |
| `playerUpdate` | `PlayerRepository.update` | Met à jour partiellement le profil |
| `playerDelete` | `PlayerRepository.delete` | Passe le statut à `supprimé` |
| `playerStatisticsUpdate` | `PlayerRepository.updateStatistics` | Remplace les statistiques cumulées |
| `playerJoinTeam` | `PlayerRepository.addTeam` | Ajoute une association joueur-équipe |
| `playerLeaveTeam` | `PlayerRepository.removeTeam` | Supprime une association joueur-équipe |
| `playerGetAll` | `PlayerRepository.findAll` | Liste tous les joueurs |
| `playerGetById` | `PlayerRepository.findById` | Récupère un joueur par UUID |
| `playerCount` | `PlayerRepository.count` | Compte les joueurs |
| `playerReset` | `PlayerRepository.deleteAll` | Réinitialise les données joueurs |

## Procédures Métier Équipe

Ces procédures existent dans le même schéma car la relation joueur-équipe a besoin d'équipes valides.

| Procédure | Rôle |
|---|---|
| `teamCreate` | Créer une équipe |
| `teamUpdateName` | Modifier le nom d'une équipe |
| `teamDelete` | Supprimer une équipe et ses associations |
| `teamGetAll` | Lister les équipes |
| `teamGetById` | Récupérer une équipe |

## Flux SQL par Route

| Route | Procédure principale |
|---|---|
| `GET /players` | `playerGetAll()` |
| `POST /players` | `playerCreate(...)` |
| `GET /players/{id}` | `playerGetById(?)` |
| `PUT /players/{id}` | `playerUpdate(...)` |
| `DELETE /players/{id}` | `playerDelete(?)` |
| `POST /players/{id}/statistics` | `playerStatisticsUpdate(...)` |
| `POST /events/teams/player-joined` | `playerJoinTeam(...)` |
| `POST /events/teams/player-left` | `playerLeaveTeam(...)` |
| `GET /admin/players/count` | `playerCount()` |
| `DELETE /admin/players` | `playerReset(?)` |

## Initialisation Locale

Depuis `manage-player` :

```bash
docker compose up -d
```

Le conteneur MariaDB exécute automatiquement :

1. création du schéma, des tables et des données de démonstration ;
2. création des vues ;
3. création des procédures stockées.

## Point à Présenter

La base ne sert pas seulement à stocker. Elle protège aussi les invariants métier critiques : cohérence des statistiques, unicité fonctionnelle, existence des entités et intégrité des associations.
