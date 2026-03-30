# Assists

**Résumé métier**

Représente le nombre de passes décisives réalisées par un joueur.

**Attributs**

- value - *Number*

**Invariants**

- value doit être non vide
- value doit être un nombre entier
- value doit être supérieur ou égal à 0

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Assists.schema.json`
- **Fixture valide** : `tests/fixtures/Assists.valid.json`
- **Fixture invalide** : `tests/fixtures/Assists.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un assists valide est accepté
- Vérifier que assists = 0 est accepté
- Vérifier qu’un assists négatif est rejeté
- Vérifier qu’un assists décimal est rejeté
- Vérifier qu’un assists non numérique est rejeté