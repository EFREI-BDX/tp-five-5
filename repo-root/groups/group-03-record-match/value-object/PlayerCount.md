# PlayerCount

**Résumé métier**

Nombre de joueurs nécessaires pour enregistrer un type d'événement.

Ce Value Object permet de savoir si un événement doit être associé à aucun joueur, un joueur ou deux joueurs.

**Utilisé par**

- `Event.nbPlayers`
- validation de `MatchEvent.player1Id`
- validation de `MatchEvent.player2Id`

**Valeur portée**

- entier

**Invariants**

- doit être renseigné
- doit être un entier
- doit être supérieur ou égal à 0
- doit être inférieur ou égal à 2
- les valeurs autorisées sont :
  - `0` : aucun joueur requis
  - `1` : un joueur requis
  - `2` : deux joueurs requis

**Tests minimaux attendus**

- **createValidZero** - création avec 0 ne lève pas d'exception.
- **createValidOne** - création avec 1 ne lève pas d'exception.
- **createValidTwo** - création avec 2 ne lève pas d'exception.
- **createNegativeThrows** - valeur négative lève une exception métier.
- **createGreaterThanTwoThrows** - valeur supérieure à 2 lève une exception métier.
- **createDecimalThrows** - valeur décimale lève une exception métier.
- **requiresNoPlayer** - retourne vrai si la valeur est 0.
- **requiresOnePlayer** - retourne vrai si la valeur est 1.
- **requiresTwoPlayers** - retourne vrai si la valeur est 2.