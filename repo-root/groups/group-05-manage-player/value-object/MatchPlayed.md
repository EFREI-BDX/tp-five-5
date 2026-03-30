# MatchesPlayed

**Résumé métier**

Représente le nombre de matchs joués par un joueur.

**Attributs**

- value - *Number*

**Invariants**

- value doit être non vide
- value doit être un nombre entier
- value doit être supérieur ou égal à 0

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/MatchesPlayed.schema.json`
- **Fixture valide** : `tests/fixtures/MatchesPlayed.valid.json`
- **Fixture invalide** : `tests/fixtures/MatchesPlayed.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un matchesPlayed valide est accepté
- Vérifier que matchesPlayed = 0 est accepté
- Vérifier qu’un matchesPlayed négatif est rejeté
- Vérifier qu’un matchesPlayed décimal est rejeté
- Vérifier qu’un matchesPlayed non numérique est rejeté