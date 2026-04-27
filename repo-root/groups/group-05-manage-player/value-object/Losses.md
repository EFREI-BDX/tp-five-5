# Losses

**Résumé métier**

Représente le nombre de défaites d'un joueur.

**Attributs**

- value - *Number*

**Invariants**

- value doit être non vide
- value doit être un nombre entier
- value doit être supérieur ou égal à 0

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Losses.schema.json`
- **Fixture valide** : `tests/fixtures/Losses.valid.json`
- **Fixture invalide** : `tests/fixtures/Losses.invalid.json`

**Tests minimaux attendus**

- Vérifier qu'un losses valide est accepté
- Vérifier que losses = 0 est accepté
- Vérifier qu'un losses négatif est rejeté
- Vérifier qu'un losses décimal est rejeté
- Vérifier qu'un losses non numérique est rejeté
