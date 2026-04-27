# TeamId

**Résumé métier**

Identifiant unique d'une équipe.

Dans le contexte Record Match, l'équipe est référencée mais n'est pas forcément gérée localement.

**Utilisé par**

- `Match.team1Id`
- `Match.team2Id`
- `Player.teamId`

**Valeur portée**

- UUID sous forme de chaîne de caractères

**Invariants**

- doit être renseigné
- doit être une chaîne non vide
- doit être un UUID valide

**Tests minimaux attendus**

- **createValid** - création avec un UUID valide ne lève pas d'exception.
- **createInvalidUuidThrows** - valeur non UUID lève une exception métier.
- **createEmptyThrows** - valeur vide lève une exception métier.
- **equalsSameValue** - deux TeamId avec la même valeur sont égaux.
- **toStringReturnsValue** - conversion en chaîne retourne l'UUID d'origine.