# MatchTeams

**Résumé métier**

Représente les deux équipes qui s'affrontent dans un match.

Ce Value Object permet de garantir qu'un match oppose bien deux équipes distinctes.

**Utilisé par**

- `Match.IdTeam1`
- `Match.IdTeam2`

**Valeurs portées**

- `team1` : TeamId
- `team2` : TeamId

**Invariants**

- `team1` doit être renseigné
- `team2` doit être renseigné
- `team1` et `team2` doivent être différents

**Tests minimaux attendus**

- **createValid** - création avec deux TeamId différents ne lève pas d'exception.
- **createSameTeamsThrows** - deux TeamId identiques lèvent une exception métier.
- **createMissingTeam1Throws** - team1 absent lève une exception métier.
- **createMissingTeam2Throws** - team2 absent lève une exception métier.
- **containsTeam** - permet de vérifier si une équipe appartient au match.
- **equalsSameValues** - deux MatchTeams avec les mêmes équipes sont égaux.