# DissolutionDate

**Résumé métier**
Une date de dissolution d'une équipe, qui doit être comprise entre le 1er janvier 2020 et la date du jour.

**Attributs**

- value — *LocalDate*

**Invariants**

- value doit être non vide
- value doit être supérieure ou égale à 2020-01-01
- value doit être inférieure ou égale à la date du jour

**Format JSON attendu**

- **Schéma** : `tests/schemas/DissolutionDate.schema.json`
- **Fixture valide** : `tests/fixtures/DissolutionDate.valid.json`
- **Fixture invalide** : `tests/fixtures/DissolutionDate.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**
