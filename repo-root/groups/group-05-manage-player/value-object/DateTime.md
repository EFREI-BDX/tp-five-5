# DateTime

**Résumé métier**

Représente une date et heure technique utilisée pour tracer la création et la mise à jour d’un joueur.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit respecter un format date-heure valide (ISO 8601 recommandé)
- value doit représenter une date-heure valide

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/DateTime.schema.json`
- **Fixture valide** : `tests/fixtures/DateTime.valid.json`
- **Fixture invalide** : `tests/fixtures/DateTime.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un datetime valide est accepté
- Vérifier qu’un datetime vide est rejeté
- Vérifier qu’un datetime au mauvais format est rejeté
- Vérifier qu’un datetime invalide est rejeté
- Vérifier qu’un datetime non string est rejeté