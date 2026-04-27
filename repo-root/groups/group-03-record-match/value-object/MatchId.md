# MatchId

**Résumé métier**

Identifiant unique d'un match.

**Utilisé par**

- `Match.matchId`
- `MatchEvent.matchId`

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
- **equalsSameValue** - deux MatchId avec la même valeur sont égaux.
- **toStringReturnsValue** - conversion en chaîne retourne l'UUID d'origine.