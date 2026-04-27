# EventParticipants

**Résumé métier**

Représente les joueurs impliqués dans un événement de match.

Ce Value Object encapsule la règle selon laquelle le nombre de joueurs renseignés doit correspondre au nombre de joueurs requis par le type d'événement.

**Utilisé par**

- `MatchEvent.IdPlayer1`
- `MatchEvent.IdPlayer2`
- validation avec `Event.NbPlayer`

**Valeurs portées**

- `player1` : PlayerId optionnel
- `player2` : PlayerId optionnel

**Invariants**

- si `PlayerCount = 0`, aucun joueur n'est obligatoire
- si `PlayerCount = 1`, `player1` est obligatoire
- si `PlayerCount = 2`, `player1` et `player2` sont obligatoires
- si `player1` et `player2` sont renseignés, ils doivent être différents

**Tests minimaux attendus**

- **createValidWithoutPlayer** - création avec PlayerCount = 0 sans joueur ne lève pas d'exception.
- **createValidWithOnePlayer** - création avec PlayerCount = 1 et player1 renseigné ne lève pas d'exception.
- **createValidWithTwoPlayers** - création avec PlayerCount = 2, player1 et player2 renseignés ne lève pas d'exception.
- **createMissingPlayer1Throws** - PlayerCount = 1 ou 2 sans player1 lève une exception métier.
- **createMissingPlayer2Throws** - PlayerCount = 2 sans player2 lève une exception métier.
- **createSamePlayersThrows** - player1 égal à player2 lève une exception métier.
- **hasNoPlayer** - retourne vrai si aucun joueur n'est renseigné.
- **hasOnePlayer** - retourne vrai si un seul joueur est renseigné.
- **hasTwoPlayers** - retourne vrai si deux joueurs sont renseignés.