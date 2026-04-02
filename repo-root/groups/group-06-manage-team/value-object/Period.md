# Period

**Résumé métier**
Une date de crétation et de dissolution d'une équipe, qui doit être comprise entre le 1er janvier 2020 et la date du
jour.

**Attributs**

- creationDate — *LocalDate*
- dissolutionDate — *LocalDate*

**Invariants**

- creationDate doit être non vide
- dissolutionDate doit être supérieure ou égale à creationDate
- dissolutionDate doit être supérieure ou égale à 2020-01-01
- dissolutionDate doit être inférieure ou égale à la date du jour
- creationDate doit être supérieure ou égale à 2020-01-01
- creationDate doit être inférieure ou égale à la date du jour

**Format JSON attendu**

- **Schéma** : `tests/schemas/Period.schema.json`
- **Fixture valide** : `tests/fixtures/Period.valid.json`
- **Fixture invalide** : `tests/fixtures/Period.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**
