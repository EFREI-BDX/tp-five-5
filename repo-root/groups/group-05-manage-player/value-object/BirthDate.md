# BirthDate

**Résumé métier**

Représente la date de naissance d’un joueur.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit respecter le format `JJ/MM/AAAA`
- value doit représenter une date valide
- value ne doit pas être dans le futur

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/BirthDate.schema.json`
- **Fixture valide** : `tests/fixtures/BirthDate.valid.json`
- **Fixture invalide** : `tests/fixtures/BirthDate.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’une birthDate valide est acceptée
- Vérifier qu’une birthDate vide est rejetée
- Vérifier qu’une birthDate au mauvais format est rejetée
- Vérifier qu’une birthDate invalide est rejetée
- Vérifier qu’une birthDate future est rejetée
- Vérifier qu’une birthDate non string est rejetée