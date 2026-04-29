# OccuredAt

**Résumé métier**

Date d'occurrence de l'événement.

Ce Value Object représente une date/heure métier stockée en base dans la colonne SQL `occuredAt` (`date`).

**Utilisé par**

- `MatchEvent.occuredAt`

**Valeur portée**

- date

**Invariants**

- doit être renseigné
- doit être une date/heure valide
- doit représenter une date/heure valide d'occurrence de l'événement

**Tests minimaux attendus**

- **createValid** - création avec une date/heure valide ne lève pas d'exception.
- **createNullThrows** - valeur nulle lève une exception métier.
- **createInvalidDateTimeThrows** - date/heure invalide lève une exception métier.
- **toIsoStringReturnsValue** - conversion en chaîne ISO conserve la valeur.
- **equalsSameDate** - deux OccuredAt représentant la même date sont égaux.