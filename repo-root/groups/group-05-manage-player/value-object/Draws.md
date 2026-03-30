# Draws

**Résumé métier**

Représente le nombre d'égalités d'un joueur.

**Attributs**

- value - *Number*

**Invariants**

- value doit être non vide
- value doit être un nombre entier
- value doit être supérieur ou égal à 0

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Draws.schema.json`
- **Fixture valide** : `tests/fixtures/Draws.valid.json`
- **Fixture invalide** : `tests/fixtures/Draws.invalid.json`

**Tests minimaux attendus**

- Vérifier qu'un draws valide est accepté
- Vérifier que draws = 0 est accepté
- Vérifier qu'un draws négatif est rejeté
- Vérifier qu'un draws décimal est rejeté
- Vérifier qu'un draws non numérique est rejeté
