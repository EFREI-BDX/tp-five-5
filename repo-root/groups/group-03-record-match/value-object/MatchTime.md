# MatchTime

**Résumé métier**

Représente le temps de jeu au moment d'un événement dans un match.

Ce Value Object encapsule la minute, la seconde et la période (FIRST_HALF / SECOND_HALF) calculées à partir de l'heure de début du match, de sa durée prévue et de l'horodatage de l'événement.

**Utilisé par**

- Construction des payloads outbound (`MatchStarted`, `MatchFinished`, `GoalScored`, `YellowCard`, etc.)
- Tout événement outbound nécessitant un champ `matchTime`

**Valeurs portées**

- `minute` : entier, minute écoulée depuis le début de la période
- `second` : entier, seconde dans la minute courante
- `period` : chaîne de caractères — `FIRST_HALF` ou `SECOND_HALF`

**Invariants**

- `minute` doit être supérieur ou égal à 0
- `second` doit être compris entre 0 et 59 inclus
- `period` doit être `FIRST_HALF` ou `SECOND_HALF`

**Règle de calcul**

```
elapsed      = occuredAt - startedAt  (en secondes)
halfDuration = scheduledDurationMinutes / 2  (en secondes)

si elapsed <= halfDuration :
    period  = FIRST_HALF
    minute  = elapsed / 60
    second  = elapsed % 60
sinon :
    period  = SECOND_HALF
    minute  = (elapsed - halfDuration) / 60
    second  = (elapsed - halfDuration) % 60
```

**Tests minimaux attendus**

- **computeFirstHalf** - un événement dans la première moitié du match retourne `FIRST_HALF`.
- **computeSecondHalf** - un événement dans la seconde moitié retourne `SECOND_HALF`.
- **computeAtKickoff** - occuredAt égal à startedAt retourne minute=0, second=0, period=FIRST_HALF.
- **computeAtHalfTime** - occuredAt exactement à la mi-temps retourne minute=0, second=0, period=SECOND_HALF.
- **createInvalidMinuteThrows** - minute négative lève une exception métier.
- **createInvalidSecondThrows** - seconde hors [0,59] lève une exception métier.
- **createInvalidPeriodThrows** - period autre que FIRST_HALF ou SECOND_HALF lève une exception métier.
