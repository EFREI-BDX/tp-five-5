# EventName

**Résumé métier**

Nom ou libellé métier d'un type d'événement.

Exemples : but, faute, arrêt, passe décisive, début du match, fin du match.

**Utilisé par**

- `Event.Name`

**Valeur portée**

- chaîne de caractères

**Invariants**

- doit être renseigné
- ne doit pas être vide
- ne doit pas contenir uniquement des espaces
- doit être suffisamment explicite pour être affiché ou exploité dans les statistiques

**Tests minimaux attendus**

- **createValid** - création avec un nom non vide ne lève pas d'exception.
- **createEmptyThrows** - chaîne vide lève une exception métier.
- **createBlankThrows** - chaîne contenant uniquement des espaces lève une exception métier.
- **trimValue** - les espaces inutiles en début et fin peuvent être supprimés.
- **toStringReturnsValue** - conversion en chaîne retourne le nom de l'événement.