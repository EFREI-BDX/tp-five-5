# Projet et Architecture

## Objectif du Module

`manage-player` est le service responsable de la gestion des joueurs dans Efrei Five.

Il couvre :

- la création d'un joueur ;
- la consultation d'un joueur ou de la liste des joueurs ;
- la modification partielle du profil ;
- la suppression logique ;
- la mise à jour des statistiques ;
- la synchronisation des liens joueur-équipe depuis des événements entrants.

Le service ne gère pas la création des équipes, les matchs complets, les réservations ou l'authentification utilisateur avancée.

## Stack Technique

| Élément | Choix |
|---|---|
| Langage | Java 17 |
| Framework | Spring Boot 3.3.5 |
| Build | Maven |
| API | Spring Web |
| Validation | Jakarta Validation + validations métier dans le service |
| Sécurité | Filtre API key applicatif |
| Base de données | MariaDB 11 |
| Accès données | Spring JDBC `JdbcTemplate` |
| Tests manuels | CLI Bash + collection Bruno |

## Architecture Applicative

Le module suit une architecture en couches, simple à présenter et à maintenir.

```text
Client HTTP / Event HTTP
  -> Controller / Inbound / Admin
  -> Service
  -> Mapper
  -> Repository
  -> MariaDB
```

## Responsabilités par Couche

| Couche | Packages | Responsabilité |
|---|---|---|
| API HTTP | `controller`, `admin`, `inbound` | Exposer les routes, recevoir les payloads, retourner les réponses HTTP |
| Sécurité | `security` | Vérifier le header `X-API-KEY` sur les routes protégées |
| Métier | `service` | Porter les règles métier, validations, orchestration et cas d'usage |
| Transformation | `mapper` | Construire ou fusionner les DTO |
| Contrats | `dto` | Définir les requêtes/réponses JSON |
| Persistance | `repository` | Encapsuler `JdbcTemplate` et les procédures stockées MariaDB |
| Configuration | `config` | Déclarer datasource, admin properties et clients techniques |

## Flux Principal

Exemple pour `POST /players` :

```text
PlayerController.create
  -> PlayerService.create
  -> PlayerMapper.fromCreateRequest
  -> PlayerRepository.create
  -> CALL fiveplayer.playerCreate(...)
```

Le controller ne porte pas la logique métier. Il délègue au service. Le service valide les données et orchestre. Le repository est le seul composant qui connaît les procédures stockées.

## Grands Choix d'Architecture

### Architecture en Couches

Le service sépare les responsabilités pour éviter des controllers trop lourds et pour rendre les flux faciles à expliquer.

### DTO comme Contrat Externe

Les classes `CreatePlayerRequest`, `PlayerDto`, `UpdatePlayerResponse`, etc. stabilisent le contrat HTTP. Le service n'expose pas directement les tables SQL.

### Repository JDBC Centralisé

`PlayerRepository` centralise tout l'accès aux données. Les controllers et services ne manipulent jamais de SQL directement.

### Procédures Stockées

Les règles fortes liées à la donnée sont aussi protégées côté MariaDB : unicité, existence des joueurs, cohérence des statistiques, association joueur-équipe.

### Suppression Logique

Un joueur supprimé n'est pas retiré physiquement de la base. Son statut passe à `supprimé`, ce qui conserve l'historique et évite de casser les références.

### Événements Entrants en HTTP

Les événements `PlayerJoinedTeam` et `PlayerLeftTeam` sont simulés par endpoints HTTP. Ce choix permet de démontrer l'intégration sans installer de bus de messages.

### Événements Sortants Contractualisés

Les événements sortants (`PlayerCreated`, `PlayerDeleted`, `PlayerNameUpdated`) sont documentés comme contrats, mais ne sont pas encore publiés par le code applicatif.

## Sécurité

Toutes les routes métier et techniques sont protégées par :

```http
X-API-KEY: dev-api-key
```

Exceptions :

- `GET /health` ;
- routes internes `/error`.

Le filtre concerné est :

```text
security/ApiKeyFilter.java
```

## Gestion des Erreurs

Les erreurs sont centralisées dans :

```text
controller/GlobalExceptionHandler.java
```

Le format de réponse est homogène :

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Les donnees fournies sont invalides.",
  "details": []
}
```

## Diagramme de Classes

Le diagramme complet est disponible ici :

- [Diagramme de classes](./class-diagram.md)
- [Source PlantUML](./class-diagram.puml)

## Limites Actuelles

- les événements sortants ne sont pas encore publiés sur un bus ;
- les événements entrants sont exposés en HTTP au lieu d'un broker ;
- la sécurité API key reste adaptée à une démonstration, pas à une production ;
- les classes `entity` et `valueobject` documentent l'intention domaine mais sont exclues de la compilation Maven.
