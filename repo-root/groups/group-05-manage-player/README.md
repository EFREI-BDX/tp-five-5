# Group 05 - Manage Player

**Contexte**

Ce groupe porte le bounded context `manage-player` du projet Efrei Five. Le service gère les profils joueurs, leurs statistiques, leur statut et leur association avec les équipes.

**Membres**

- **LUXEY Aurélien** — identifiant GitHub
- **BERGER Florian** — identifiant GitHub
- **IBOS Geoffrey** — identifiant GitHub
- **DEBEURET Oscar** — identifiant GitHub
- **MAURY Louis** — identifiant GitHub

**Stack choisie** : Java 17, Spring Boot, Maven, MariaDB, Docker Compose.

**Livrables attendus**

- `domain-summary.md`
- `contexts-map.md`
- `value-objects/*.md` (un fichier par VO)
- `openapi.yaml`
- `mock/postman-collection.json`
- `tests/schemas/*.schema.json` et `tests/fixtures/*.valid.json` / `*.invalid.json`
- `CONTRIBUTION.md`
- `manage-player/doc/*.md` pour la documentation technique et le support de présentation.

**Commandes utiles**

Depuis `manage-player` :

```bash
docker compose up -d
mvn spring-boot:run
```

Documentation principale :

```text
manage-player/doc/README.md
```

Structure recommandée pour la présentation :

- `manage-player/doc/project-and-architecture.md` : projet et architecture.
- `manage-player/doc/database.md` : base de données, tables, vues et procédures.
- `manage-player/doc/routes-and-events.md` : routes et événements.

**Notes**

Les événements sortants sont contractualisés dans `event/out`, mais ils ne sont pas encore publiés par le code applicatif. Les événements entrants d'équipe sont simulés par endpoints HTTP dans `manage-player`.
