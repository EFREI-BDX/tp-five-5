# Level

**Résumé métier**

Représente le niveau sportif d’un joueur dans le système.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit appartenir à une liste de niveaux autorisés par le système
- value ne doit pas contenir uniquement des espaces

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Level.schema.json`
- **Fixture valide** : `tests/fixtures/Level.valid.json`
- **Fixture invalide** : `tests/fixtures/Level.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un level valide est accepté
- Vérifier qu’un level vide est rejeté
- Vérifier qu’un level hors enum est rejeté
- Vérifier qu’un level non string est rejeté