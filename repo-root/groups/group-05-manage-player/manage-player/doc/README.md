# Documentation Manage Player

Ce dossier documente uniquement le service `manage-player`.

`manage-player` est le service responsable de la gestion des joueurs dans le projet Efrei Five. Il expose une API HTTP Spring Boot permettant de créer, consulter, modifier et supprimer logiquement des joueurs, ainsi que de mettre à jour leurs statistiques.

## Lecture Conseillée

La documentation est organisée en trois parties principales.

### 1. Projet et Architecture

- [Projet et architecture](./project-and-architecture.md) : rôle du service, stack, couches applicatives, grands choix d'architecture, sécurité et limites.
- [Diagramme de classes](./class-diagram.md) : vue UML du module.
- [Source PlantUML](./class-diagram.puml) : fichier source du diagramme.

### 2. Base de Données

- [Base de données](./database.md) : tables, relations, vues, procédures stockées et lien avec le repository Java.

### 3. Routes et Événements

- [Routes et événements](./routes-and-events.md) : routes HTTP, classes appelées, procédures SQL, événements entrants et événements sortants documentés.
- [API HTTP](./api.md) : exemples de requêtes et réponses.

## Documents Complémentaires

- [Support de présentation](./presentation.md) : version courte pour slides.
- [Mockups](./mockups.md) : propositions d'écrans pour illustrer les cas d'usage.
- [Vue d'ensemble](./overview.md) : périmètre fonctionnel.
- [Architecture applicative détaillée](./architecture.md) : organisation des packages.
- [Infrastructure](./infrastructure.md) : lancement local et configuration.
- [Modèle domaine](./domain-model.md) : agrégats, DTO et value objects.
- [Sécurité et erreurs](./security-and-errors.md) : API key et réponses d'erreur.
- [Choix techniques](./technical-decisions.md) : justification des décisions.
- [Exploitation et tests](./operations-and-tests.md) : commandes utiles.

## Lecture Rapide

Le service est une application Java 17 / Spring Boot 3.3.5 construite avec Maven. La persistance passe par MariaDB via `JdbcTemplate` et des procédures stockées du schéma `fiveplayer`.

Le module suit une architecture en couches :

```text
HTTP / Events
    -> controller / inbound / admin
    -> service
    -> repository
    -> MariaDB procedures
```

Les routes métier sont protégées par une clé API transmise dans le header `X-API-KEY`. La route `/health` reste publique pour permettre la supervision. Les événements entrants d'équipe sont exposés sous forme d'endpoints HTTP, ce qui simule un bus de messages tout en restant simple à démontrer.
