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

- **Schéma JSON** : `tests/schemas/Mvps.schema.json`
- **Fixture valide** : `tests/fixtures/Mvps.valid.json`
- **Fixture invalide** : `tests/fixtures/Mvps.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un mvps valide est accepté
- Vérifier que mvps = 0 est accepté
- Vérifier qu’un mvps négatif est rejeté
- Vérifier qu’un mvps décimal est rejeté
- Vérifier qu’un mvps non numérique est rejeté