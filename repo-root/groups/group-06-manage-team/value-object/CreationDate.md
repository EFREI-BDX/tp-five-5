# CreationDate

**Résumé métier**
Une date de création d'une équipe. Elle doit être comprise entre le 1er janvier 2020 et la date du jour.

**Attributs**

- value — *LocalDate*

**Invariants**

- value doit être non vide
- value doit être supérieure ou égale à 2020-01-01
- value doit être inférieure ou égale à la date du jour

**Format JSON attendu**

- **Schéma** : `tests/schemas/label.schema.json`
- **Fixture valide** : `tests/fixtures/label.valid.json`
- **Fixture invalide** : `tests/fixtures/label.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**
