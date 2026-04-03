# Manage Player API

## Lancement

Lancer l'application Spring Boot depuis `manage-player` :

```bash
mvn spring-boot:run
```

Par defaut, l'API ecoute sur `http://localhost:8080`.

## Securite

Toutes les routes metier demandent le header suivant :

```http
X-API-KEY: dev-api-key
```

La route `GET /health` est publique.

La cle par defaut est definie dans `src/main/resources/application.properties` :

```properties
app.security.api-key=dev-api-key
```

## Routes

### Health

- `GET /health`

Exemple :

```bash
curl http://localhost:8080/health
```

### Players

- `POST /players`
- `GET /players/{id}`
- `PUT /players/{id}`
- `DELETE /players/{id}`
- `POST /players/{id}/statistics`

Exemple creation :

```bash
curl -X POST http://localhost:8080/players \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-api-key" \
  -d '{
    "firstName": "Jean",
    "lastName": "Dupont",
    "email": "jean.dupont@example.com",
    "phone": "+33612345678",
    "birthDate": "15/06/1995",
    "gender": "homme",
    "height": 178.5
  }'
```

Exemple lecture :

```bash
curl http://localhost:8080/players/<player-id> \
  -H "X-API-KEY: dev-api-key"
```

Exemple mise a jour partielle :

```bash
curl -X PUT http://localhost:8080/players/<player-id> \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-api-key" \
  -d '{
    "phone": "+33698765432"
  }'
```

Exemple suppression logique :

```bash
curl -X DELETE http://localhost:8080/players/<player-id> \
  -H "X-API-KEY: dev-api-key"
```

Exemple mise a jour des statistiques :

```bash
curl -X POST http://localhost:8080/players/<player-id>/statistics \
  -H "Content-Type: application/json" \
  -H "X-API-KEY: dev-api-key" \
  -d '{
    "matchesPlayed": 10,
    "goalsScored": 4,
    "assists": 2,
    "wins": 6
  }'
```

## Script CLI

Le script `api-cli.sh` permet de tester rapidement l'API depuis le terminal.

Afficher l'aide :

```bash
./api-cli.sh help
```

Configurer l'URL et la cle API :

```bash
BASE_URL=http://localhost:8080 API_KEY=dev-api-key ./api-cli.sh help
```

Exemples :

```bash
./api-cli.sh health
./api-cli.sh players create Jean Dupont jean.dupont@example.com +33612345678 homme 15/06/1995 178.5
./api-cli.sh players get <player-id>
./api-cli.sh players update <player-id> - - new.email@example.com - - - -
./api-cli.sh players stats <player-id> 10 4 2 6
./api-cli.sh players delete <player-id>
./api-cli.sh demo
```

Le mot-cle `-` sur `players update` signifie "ne pas modifier ce champ".
