# Infrastructure

## Stack Technique

Le service utilise :

- Java 17 ;
- Spring Boot 3.3.5 ;
- Maven ;
- Spring Web pour l'API HTTP ;
- Spring Validation pour la validation des payloads ;
- Spring JDBC pour l'accès base de données ;
- MariaDB 11 pour la persistance.

## Structure du Module

Le module Maven est situé dans :

```text
repo-root/groups/group-05-manage-player/manage-player
```

Le point d'entrée applicatif est :

```text
src/main/java/fr/efreifive/manageplayer/ManagePlayerApplication.java
```

## Lancement Local

Depuis le dossier `manage-player`, démarrer d'abord la base :

```bash
docker compose up -d
```

Puis lancer l'application :

```bash
mvn spring-boot:run
```

Par défaut, Spring Boot expose l'application sur :

```text
http://localhost:8080
```

## Configuration

La configuration locale principale se trouve dans :

```text
src/main/resources/application.properties
```

Valeur actuelle :

```properties
app.security.api-key=dev-api-key
spring.datasource.url=jdbc:mariadb://localhost:3306/fiveplayer
spring.datasource.username=jad_efrei_five_2526
spring.datasource.password=jad_efrei_five_2526
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
```

La clé est utilisée par `ApiKeyFilter` pour protéger les routes métier. Les propriétés `spring.datasource.*` configurent `JdbcTemplate`.

## Persistance

La persistance est portée par MariaDB :

```text
PlayerRepository -> JdbcTemplate -> procédures stockées fiveplayer
```

Les scripts SQL sont montés dans le container MariaDB :

```text
../sql/player-create.sql
../sql/views.sql
../sql/stored-procedures.sql
```

Les données sont lues via des vues (`PlayerView`, `PlayerStatisticsView`, `PlayerTeamView`) et modifiées via procédures stockées (`playerCreate`, `playerUpdate`, `playerDelete`, `playerStatisticsUpdate`, `playerJoinTeam`, `playerLeaveTeam`).

## Base de Données Locale

Le fichier `docker-compose.yml` expose MariaDB sur :

```text
localhost:3306
```

Le script d'initialisation crée un jeu de données de démonstration avec plusieurs joueurs, leurs statistiques et les tables nécessaires.

## Dépendances Maven

Les dépendances principales sont :

- `spring-boot-starter-web` : création de l'API REST ;
- `spring-boot-starter-validation` : annotations de validation sur les DTO ;
- `spring-boot-starter-jdbc` : accès SQL via `JdbcTemplate` ;
- driver MariaDB : connexion à la base locale.

## Healthcheck

L'endpoint suivant est public :

```http
GET /health
```

Réponse attendue :

```json
{
  "status": "UP"
}
```

Cette route est exclue du filtre API key pour permettre une supervision simple.
