# Exploitation et Tests

## Compiler

Depuis le dossier `manage-player` :

```bash
mvn -q -DskipTests compile
```

Cette commande vérifie que le module compile sans lancer les tests.

## Lancer l'Application

Depuis `manage-player`, démarrer MariaDB :

```bash
docker compose up -d
```

Puis lancer Spring Boot :

```bash
mvn spring-boot:run
```

URL locale :

```text
http://localhost:8080
```

## Tester le Healthcheck

```bash
curl http://localhost:8080/health
```

Réponse attendue :

```json
{
  "status": "UP"
}
```

## Tester avec le Script CLI

Le script suivant existe à la racine du module :

```text
api-cli.sh
```

Exemples :

```bash
./api-cli.sh health
./api-cli.sh players create Jean Dupont jean.dupont@example.com +33612345678 homme 15/06/1995 178.5
./api-cli.sh players get <player-id>
./api-cli.sh players update <player-id> - - new.email@example.com - - - -
./api-cli.sh players stats <player-id> 10 4 2 6 2 2 1
./api-cli.sh players delete <player-id>
```

## Variables Utiles

```bash
BASE_URL=http://localhost:8080
API_KEY=dev-api-key
```

Exemple :

```bash
BASE_URL=http://localhost:8080 API_KEY=dev-api-key ./api-cli.sh help
```

## Vérifications Manuelles Conseillées

Scénario minimal :

1. démarrer l'application ;
2. appeler `/health` ;
3. créer un joueur ;
4. récupérer le joueur créé ;
5. modifier un champ ;
6. mettre à jour les statistiques ;
7. simuler `player-joined` puis vérifier que `teamIds` évolue ;
8. supprimer logiquement le joueur ;
9. vérifier qu'une modification après suppression est refusée.

## Limites Actuelles

Le service fonctionne, mais plusieurs points restent à renforcer :

- pas de tests automatisés dans le module applicatif ;
- pas de publication réelle d'événements sortants ;
- sécurité API key simplifiée ;
- routes inbound exposées en HTTP plutôt que via un bus de messages ;
- value objects et entities exclus de la compilation Maven.

## Évolutions Possibles

Les prochaines étapes techniques naturelles seraient :

- ajouter des tests unitaires sur `PlayerService` ;
- ajouter des tests d'intégration sur `PlayerController` ;
- renforcer les tests repository avec MariaDB ;
- publier réellement les événements `PlayerCreated`, `PlayerDeleted` et `PlayerNameUpdated` ;
- documenter les endpoints inbound dans le contrat OpenAPI ;
- externaliser la clé API via variable d'environnement.
