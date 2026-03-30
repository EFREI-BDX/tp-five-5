# Wins

**Résumé métier**

Représente le nombre de victoires d’un joueur.

**Attributs**

- value - *Number*

**Invariants**

- value doit être non vide
- value doit être un nombre entier
- value doit être supérieur ou égal à 0

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Wins.schema.json`
- **Fixture valide** : `tests/fixtures/Wins.valid.json`
- **Fixture invalide** : `tests/fixtures/Wins.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un wins valide est accepté
- Vérifier que wins = 0 est accepté
- Vérifier qu’un wins négatif est rejeté
- Vérifier qu’un wins décimal est rejeté
- Vérifier qu’un wins non numérique est rejeté