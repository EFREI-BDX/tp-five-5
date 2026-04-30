# Architecture Applicative

## Vue Globale

`manage-player` est une application Spring Boot organisée en couches. Chaque package porte une responsabilité précise afin d'éviter de concentrer la logique métier dans les controllers.

```text
fr.efreifive.manageplayer
├── admin
├── config
├── console
├── controller
│   └── command
├── dto
├── entity
├── inbound
├── mapper
├── repository
│   └── result
├── security
├── service
└── valueobject
```

## Rôle des Packages

### `controller`

Contient les endpoints HTTP publics :

- `PlayerController` expose les opérations métier sur `/players`.
- `HealthController` expose `/health`.
- `GlobalExceptionHandler` centralise les erreurs HTTP.
- `DomainErrorCodeHttpStatusMapper` traduit les erreurs métier en statuts HTTP.

Le controller ne doit pas porter la logique métier profonde. Il reçoit une requête, délègue au service, puis retourne une réponse HTTP.

### `controller.command`

Contient `PlayerCommand` et `PlayerCommandResult`. Ces types représentent les actions métier sous forme d'objets :

- création ;
- mise à jour ;
- suppression ;
- mise à jour des statistiques.

Ils préparent l'ajout d'autres adapters capables d'émettre les mêmes commandes. Dans l'état actuel, les routes HTTP appellent directement les méthodes dédiées de `PlayerService`.

### `service`

Contient la logique métier :

- validation des données ;
- règles de suppression logique ;
- règles de mise à jour des statistiques ;
- orchestration entre repository et mapper ;
- synchronisation des événements d'équipe.

Les interfaces `IPlayerService`, `IPlayerAdminService` et `IPlayerSyncService` définissent les contrats utilisés par les controllers et adapters.

### `repository`

Contient la couche d'accès aux données.

Actuellement, `PlayerRepository` utilise `JdbcTemplate` et appelle les procédures stockées MariaDB du schéma `fiveplayer`.

Le repository reste isolé derrière une classe dédiée. Les controllers et services ne connaissent donc ni les tables, ni les vues, ni les détails SQL.

### `mapper`

`PlayerMapper` centralise la création et la transformation des DTO.

Cela évite de dupliquer la reconstruction de `PlayerDto` dans plusieurs classes et rend les mutations explicites :

- création depuis `CreatePlayerRequest` ;
- fusion avec `UpdatePlayerRequest` ;
- changement de statut ;
- changement de statistiques ;
- changement des équipes associées.

### `dto`

Contient les objets échangés par l'API :

- requêtes entrantes ;
- réponses sortantes ;
- représentation JSON des joueurs et statistiques ;
- structure d'erreur standardisée.

Les DTO stabilisent le contrat HTTP et évitent d'exposer directement des classes internes.

### `inbound`

Contient les endpoints consommant des événements externes simulés en HTTP.

`TeamEventsInboundController` reçoit des événements venant du contexte équipe :

- joueur ajouté à une équipe ;
- joueur retiré d'une équipe.

Ces endpoints modifient uniquement l'association joueur-équipe en base via les procédures `playerJoinTeam` et `playerLeaveTeam`.

### `admin`

Expose des endpoints d'administration :

- comptage des joueurs ;
- reset du repository.

Ces endpoints sont désactivables via configuration.

### `config`

Contient la configuration Spring :

- `AdminProperties` pour les options d'administration ;
- `DataSourceConfig` pour préparer une configuration JDBC ;
- `WebClientConfig` pour exposer un builder HTTP si le service doit appeler d'autres services.

### `security`

Contient `ApiKeyFilter`, un filtre HTTP qui vérifie la présence du header `X-API-KEY`.

## Flux d'une Création de Joueur

```text
HTTP POST /players
    ↓
PlayerController
    ↓
PlayerService
    ↓
Validation métier
    ↓
PlayerMapper
    ↓
PlayerRepository
    ↓
CALL fiveplayer.playerCreate(...)
    ↓
CreatePlayerResponse
```

## Flux d'un Événement Équipe

```text
HTTP POST /events/teams/player-joined
    ↓
TeamEventsInboundController
    ↓
PlayerSyncService
    ↓
PlayerRepository
    ↓
CALL fiveplayer.playerJoinTeam(...)
```
