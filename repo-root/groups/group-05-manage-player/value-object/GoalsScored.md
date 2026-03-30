# GoalsScored

**Résumé métier**

Représente le nombre de buts marqués par un joueur.

**Attributs**

- value - *Number*

**Invariants**

- value doit être non vide
- value doit être un nombre entier
- value doit être supérieur ou égal à 0

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/GoalsScored.schema.json`
- **Fixture valide** : `tests/fixtures/GoalsScored.valid.json`
- **Fixture invalide** : `tests/fixtures/GoalsScored.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un goalsScored valide est accepté
- Vérifier que goalsScored = 0 est accepté
- Vérifier qu’un goalsScored négatif est rejeté
- Vérifier qu’un goalsScored décimal est rejeté
- Vérifier qu’un goalsScored non numérique est rejeté