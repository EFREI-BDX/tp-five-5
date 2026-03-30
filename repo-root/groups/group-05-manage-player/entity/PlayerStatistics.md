# PlayerStatistics

**Résumé métier**

Représente les statistiques de performance d’un joueur dans le système.

**Attributs**

- matchesPlayed - *VO MatchesPlayed*
- goalsScored - *VO GoalsScored*
- assists - *VO Assists*
- wins - *VO Wins*
- draws - *VO Draws*
- mvps - *VO MVPs*

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
- draws doit être non vide
- draws doit être un nombre entier supérieur ou égal à 0
- draws doit être inférieur ou égal à matchesPlayed
- mvps doit être un nombre entier supérieur ou égal à 0
- mvps doit être inférieur ou égal à matchesPlayed

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/PlayerStatistics.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerStatistics.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerStatistics.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un playerStatistics valide est accepté
- Vérifier que matchesPlayed = 0 est accepté
- Vérifier que draws = 0 est accepté
- Vérifier qu'un draws négatif est rejeté
- Vérifier que draws > matchesPlayed est rejetéeté
- Vérifier qu’un goalsScored négatif est rejeté
- Vérifier qu’un assists négatif est rejeté
- Vérifier qu’un wins négatif est rejeté
- Vérifier que wins > matchesPlayed est rejeté
- Vérifier qu’une valeur non numérique est rejetée
- Vérifier que mvps > matchesPlayed est rejeté