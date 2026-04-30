# Support de Présentation

## Message Principal

`manage-player` est le bounded context responsable du cycle de vie des joueurs dans Efrei Five. Il centralise le profil, le statut, les statistiques sportives et la relation avec les équipes.

Le service expose une API REST simple, protège ses routes par clé API et persiste les données dans MariaDB au travers de procédures stockées.

## Ce que le Service Fait

- créer un joueur avec des données validées ;
- lister et consulter les joueurs ;
- modifier partiellement un profil ;
- supprimer logiquement un joueur ;
- mettre à jour les statistiques cumulées ;
- synchroniser les associations joueur-équipe depuis des événements entrants ;
- fournir des routes admin pour compter ou réinitialiser les données en démonstration.

## Ce que le Service ne Fait Pas

- créer ou gérer les équipes ;
- gérer les matchs complets ;
- authentifier des utilisateurs avec rôles et sessions ;
- publier réellement les événements sortants sur un bus de messages ;
- afficher une interface web finale.

## Architecture en Une Phrase

Le controller reçoit la requête, le service applique les règles métier, le mapper construit les DTO, puis le repository exécute les procédures stockées MariaDB.

```text
Client HTTP
  -> PlayerController
  -> PlayerService
  -> PlayerMapper
  -> PlayerRepository
  -> fiveplayer.playerCreate / playerUpdate / ...
```

## Points Forts à Présenter

- séparation claire des responsabilités ;
- règles métier centralisées dans `PlayerService` ;
- contrat HTTP stable avec DTO dédiés ;
- erreurs homogènes via `GlobalExceptionHandler` ;
- persistance SQL encapsulée dans `PlayerRepository` ;
- scripts Bruno et CLI disponibles pour la démonstration ;
- événements entrants préparant l'intégration avec `manage-team`.

## Parcours de Démonstration

1. Appeler `GET /health` pour vérifier que le service répond.
2. Créer un joueur avec `POST /players`.
3. Lister ou consulter le joueur avec `GET /players` ou `GET /players/{id}`.
4. Modifier un champ avec `PUT /players/{id}`.
5. Mettre à jour les statistiques avec `POST /players/{id}/statistics`.
6. Simuler un événement équipe avec `POST /events/teams/player-joined`.
7. Supprimer logiquement le joueur avec `DELETE /players/{id}`.

## Phrase de Conclusion

`manage-player` fournit une base propre pour gérer les joueurs : les règles sont lisibles, les routes sont testables, la donnée est persistée et l'intégration événementielle avec les autres contextes est déjà amorcée.
