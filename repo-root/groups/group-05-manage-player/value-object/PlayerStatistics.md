# PlayerStatistics

**Résumé métier**

Représente les statistiques de performance d’un joueur dans le système.

**Attributs**

- matchesPlayed - *VO MatchesPlayed*
- goalsScored - *VO GoalsScored*
- assists - *VO Assists*
- wins - *VO Wins*

**Invariants**

- matchesPlayed doit être non vide
- matchesPlayed doit être un nombre entier supérieur ou égal à 0
- goalsScored doit être non vide
- goalsScored doit être un nombre entier supérieur ou égal à 0
- assists doit être non vide
- assists doit être un nombre entier supérieur ou égal à 0
- wins doit être non vide
- wins doit être un nombre entier supérieur ou égal à 0
- wins doit être inférieur ou égal à matchesPlayed

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/PlayerStatistics.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerStatistics.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerStatistics.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un playerStatistics valide est accepté
- Vérifier que matchesPlayed = 0 est accepté
- Vérifier qu’un matchesPlayed négatif est rejeté
- Vérifier qu’un goalsScored négatif est rejeté
- Vérifier qu’un assists négatif est rejeté
- Vérifier qu’un wins négatif est rejeté
- Vérifier que wins > matchesPlayed est rejeté
- Vérifier qu’une valeur non numérique est rejetée