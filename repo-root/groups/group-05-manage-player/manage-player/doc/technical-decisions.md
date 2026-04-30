# Choix Techniques

## Architecture en Couches

Le service est organisé autour de couches explicites :

```text
controller -> service -> repository
```

Avec des packages complémentaires :

- `dto` pour les contrats d'entrée/sortie ;
- `mapper` pour les transformations ;
- `inbound` pour les événements entrants ;
- `admin` pour les opérations techniques ;
- `security` pour le filtre API key ;
- `config` pour la configuration Spring.

Ce choix rend le code plus lisible et évite que les controllers portent trop de responsabilités.

## Alignement avec `manage-team`

L'architecture a été rapprochée de celle de `manage-team` :

- séparation des packages ;
- interfaces de services ;
- command/result pattern ;
- handler d'erreurs global ;
- configuration technique dédiée.

L'objectif est que les services du projet aient une forme commune. Cela facilite la lecture par les autres groupes et réduit le coût d'évolution.

## Repository SQL Centralisé

L'accès aux données est centralisé dans `PlayerRepository`.

Les controllers publics, les endpoints admin et les événements inbound passent par la même classe. Cela évite plusieurs chemins d'accès concurrents à la donnée.

## Procédures Stockées MariaDB

Le repository appelle des procédures stockées :

- `playerCreate` ;
- `playerUpdate` ;
- `playerDelete` ;
- `playerStatisticsUpdate` ;
- `playerJoinTeam` ;
- `playerLeaveTeam`.

Ce choix place une partie des contraintes d'intégrité au plus près de la donnée : unicité email, cohérence des statistiques, existence des joueurs et équipes.

## JDBC et Vues SQL

`JdbcTemplate` donne un accès simple aux procédures sans introduire d'ORM.

Les lectures utilisent des vues SQL qui normalisent le format attendu par l'API, notamment la date de naissance en `dd/MM/yyyy` et les timestamps en format ISO.

## DTO comme Contrat Stable

Les DTO sont utilisés pour les entrées et sorties HTTP.

Ce choix évite d'exposer directement les classes domaine ou entités. Il donne aussi plus de liberté pour changer l'implémentation interne sans casser les clients.

## Mapper Dédié

`PlayerMapper` centralise les transformations de `PlayerDto`.

Avantages :

- moins de duplication ;
- mutations explicites ;
- service plus lisible ;
- future migration vers MapStruct possible.

## Suppression Logique

La suppression d'un joueur ne retire pas la donnée. Elle change le statut en `supprimé`.

Ce choix correspond à un besoin métier fréquent : conserver l'historique et éviter de casser les références depuis d'autres contextes.

## API Key Simple

Le filtre API key est un compromis volontaire :

- rapide à implémenter ;
- suffisant pour un module local ou scolaire ;
- facile à tester avec `curl`.

Pour une production réelle, il faudrait remplacer ou compléter ce mécanisme par Spring Security, OAuth2/JWT ou une authentification centralisée.

## Command Pattern Préparatoire

`PlayerCommand` et `PlayerCommandResult` représentent les actions métier sous forme d'objets.

Ce choix rend les cas d'usage explicites et prépare des entrées multiples : HTTP, événements, CLI ou tests. Dans l'implémentation actuelle, les controllers utilisent surtout les méthodes directes de `PlayerService`, mais les types de commande restent utiles pour une évolution vers un traitement plus unifié.
