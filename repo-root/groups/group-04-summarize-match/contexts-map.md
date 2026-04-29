# Contexts Map - Match Summary

## Bounded contexts identifiés

- **record-match** - contexte amont qui publie les events de match.
- **resume-match** - contexte courant qui consomme le flux, reconstruit la timeline et produit le resume metier.
- **reporting / ranking / statistics** - contextes descendants qui exploitent la synthese de match.

## Responsabilites et frontieres

- `record-match` est la source de verite des events de jeu.
- `resume-match` ne reinterprete pas le jeu; il valide, ordonne et agrege la timeline.
- Les incoherences entre timeline et score final sont des anomalies de domaine, pas des erreurs techniques.
- La traduction du contrat d'events externes vers le modele interne appartient a un ACL inbound.

## Architecture hexagonale cible

- **Adapter entrant** : consumer HTTP d'events et endpoint query REST.
- **Port entrant (commandes)** : `ApplicationService` — cas d'usage de traitement d'event.
- **Port entrant (requetes)** : `MatchQueryService` — lecture du resume du match (CQRS).
- **Noyau metier** : regles de cohérence, score, sanctions, remplacements, statistiques derivees.
- **Port sortant (persistance)** : `MatchRepository` — event store.
- **Port sortant (publication)** : `DomainEventPublisher` — notification des contextes descendants.
- **Adapters sortants** : `SeaOrmMatchRepository` (PostgreSQL), `NoOpEventPublisher` (a remplacer par un adapter Kafka/AMQP).

## APIs produits

- `POST /events` — reception d'un event de match (validation schema + dispatch domaine).
- `GET /matches/{matchId}/summary` — lecture du resume courant du match.
- `GET /health` — controle technique.

## Events produits

- `PlayerData` — resume des statistiques par joueur (port `DomainEventPublisher` câblé, adapter NoOp par defaut, event outbound documente dans `events/outbound/PlayerData.md`).
