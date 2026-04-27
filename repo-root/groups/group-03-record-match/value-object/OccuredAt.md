# OccurredAt

**Résumé métier**

Date et heure réelle à laquelle l'événement a été enregistré.

Ce Value Object représente un instant réel dans le temps. Il ne représente pas le temps de jeu dans le match.

Le temps de jeu n'est pas porté par le contexte Record Match. Il pourra être calculé par un autre contexte à partir de l'heure de début du match et de l'heure réelle de l'événement.

Attention : si la base de données contient déjà le champ `OccuredAt`, on peut garder ce nom côté base pour éviter une migration.  
Dans le domaine, on peut utiliser le nom correctement orthographié `OccurredAt`.

**Utilisé par**

- `MatchEvent.OccuredAt`

**Valeur portée**

- date/heure

**Invariants**

- doit être renseigné
- doit être une date valide
- doit représenter l'instant réel d'enregistrement de l'événement

**Tests minimaux attendus**

- **createValid** - création avec une date valide ne lève pas d'exception.
- **createNullThrows** - valeur nulle lève une exception métier.
- **createInvalidDateThrows** - date invalide lève une exception métier.
- **toIsoStringReturnsValue** - conversion en chaîne ISO conserve la valeur.
- **equalsSameInstant** - deux OccurredAt représentant le même instant sont égaux.