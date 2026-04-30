# Modèle Domaine

## Agrégat Principal

L'agrégat principal est `Player`.

Il représente un joueur inscrit dans le système avec :

- un identifiant unique ;
- des informations personnelles ;
- des informations de contact ;
- un statut ;
- des statistiques ;
- une liste d'équipes associées.

## Données d'un Joueur

Un joueur contient :

- `id` : identifiant UUID ;
- `firstName` : prénom ;
- `lastName` : nom ;
- `email` : adresse email ;
- `phone` : numéro de téléphone ;
- `birthDate` : date de naissance au format `dd/MM/yyyy` ;
- `gender` : genre déclaré ;
- `height` : taille ;
- `status` : état métier ;
- `statistics` : statistiques du joueur ;
- `teamIds` : liste des équipes associées ;
- `createdAt` : date de création ;
- `updatedAt` : date de dernière modification.

## Statuts

Le service utilise principalement :

- `actif` : joueur utilisable et modifiable ;
- `supprimé` : joueur supprimé logiquement.

Un joueur supprimé ne peut plus être modifié via les opérations de mise à jour.

## Statistiques

Les statistiques sont représentées par `PlayerStatisticsDto` :

- `matchesPlayed` ;
- `goalsScored` ;
- `assists` ;
- `wins`.
- `losses` ;
- `draws` ;
- `mvps`.

Règles métier :

- chaque valeur doit être renseignée ;
- chaque valeur doit être positive ou nulle ;
- `wins + losses + draws` ne peut pas dépasser `matchesPlayed` ;
- `mvps` ne peut pas dépasser `matchesPlayed`.

## Validation Métier

Les validations principales sont dans `PlayerService` :

- prénom et nom obligatoires, longueur maximale 100 ;
- email obligatoire avec format valide ;
- téléphone obligatoire avec format accepté ;
- date de naissance obligatoire au format `dd/MM/yyyy` ;
- date de naissance non future ;
- genre dans la liste autorisée ;
- taille strictement positive ;
- statistiques cohérentes.

## DTO

Le service utilise des DTO pour séparer le contrat HTTP du modèle interne :

- `CreatePlayerRequest` ;
- `CreatePlayerResponse` ;
- `UpdatePlayerRequest` ;
- `UpdatePlayerResponse` ;
- `DeletePlayerResponse` ;
- `UpdatePlayerStatisticsRequest` ;
- `UpdatePlayerStatisticsResponse` ;
- `PlayerDto` ;
- `PlayerStatisticsDto` ;
- `TeamDto`.

## Value Objects

Le dossier `valueobject` contient des types métier comme :

- `FirstName` ;
- `LastName` ;
- `Email` ;
- `Phone` ;
- `BirthDate` ;
- `Gender` ;
- `Height` ;
- `Status` ;
- `TeamId` ;
- `TeamName`.

Le module Maven exclut actuellement `entity/**` et `valueobject/**` de la compilation. Ces classes documentent l'intention domaine, mais le flux applicatif actuel repose sur les DTO, services et procédures stockées.

## Relation avec Team

`manage-player` ne crée pas les équipes. Il expose seulement les identifiants d'équipes dans `teamIds`, construits à partir de la table d'association `player_team`.

La relation joueur-équipe est mise à jour via les événements entrants :

- `player-joined` appelle `fiveplayer.playerJoinTeam` ;
- `player-left` appelle `fiveplayer.playerLeaveTeam`.
