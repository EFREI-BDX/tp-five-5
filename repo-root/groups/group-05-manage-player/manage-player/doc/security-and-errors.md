# Sécurité et Gestion des Erreurs

## Sécurité par Clé API

La sécurité actuelle repose sur un filtre applicatif :

```text
security/ApiKeyFilter.java
```

Le client doit envoyer :

```http
X-API-KEY: dev-api-key
```

La valeur attendue est lue depuis :

```properties
app.security.api-key=dev-api-key
```

## Routes Exclues du Filtre

Le filtre ne s'applique pas à :

- `/health` ;
- `/error`.

Ce choix permet :

- de superviser l'application sans secret ;
- de laisser Spring gérer correctement ses routes d'erreur internes.

## Limites de cette Sécurité

La clé API actuelle est volontairement simple.

Elle convient pour :

- un environnement local ;
- un prototype ;
- une démonstration.

Elle ne suffit pas pour une production réelle, car elle ne gère pas :

- les rôles ;
- les utilisateurs ;
- la rotation de clés ;
- les permissions fines ;
- l'expiration de sessions.

## Format d'Erreur

Les erreurs HTTP retournent un `ErrorResponse` contenant :

- un code ;
- un message ;
- une liste de détails.

Exemple :

```json
{
  "code": "VALIDATION_ERROR",
  "message": "Les donnees fournies sont invalides.",
  "details": []
}
```

## Gestion Centralisée

`GlobalExceptionHandler` centralise :

- les erreurs de validation Spring ;
- les JSON invalides ;
- les `ResponseStatusException` ;
- les `ServiceOperationException` ;
- les erreurs inattendues.

Cela évite de gérer les erreurs manuellement dans chaque controller.

## Mapping Métier vers HTTP

`DomainErrorCodeHttpStatusMapper` convertit les erreurs métier :

- `PLAYER_NOT_FOUND` -> `404 Not Found` ;
- `PLAYER_ALREADY_EXISTS` -> `409 Conflict` ;
- `PLAYER_DELETED` -> `400 Bad Request` ;
- `VALIDATION_ERROR` -> `400 Bad Request` ;
- `UNAUTHORIZED` -> `401 Unauthorized` ;
- `INTERNAL_SERVER_ERROR` -> `500 Internal Server Error`.

