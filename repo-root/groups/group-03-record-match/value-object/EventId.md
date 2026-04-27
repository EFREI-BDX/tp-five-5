# EventId

**Résumé métier**

Identifiant unique d'un type d'événement enregistrable pendant un match.

**Utilisé par**

- `Event.IdEvent`
- `MatchEvent.IdEvent`

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
- **equalsSameValue** - deux EventId avec la même valeur sont égaux.
- **toStringReturnsValue** - conversion en chaîne retourne l'UUID d'origine.