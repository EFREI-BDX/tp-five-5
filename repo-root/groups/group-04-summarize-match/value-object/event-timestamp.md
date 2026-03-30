# EventTimestamp

**Résumé métier**  
Représente le moment où un évènement (`Event`) se produit pendant un match de Five.  
C’est un timestamp **relatif au match** (pas une date/heure réelle), utilisé pour ordonner la timeline et recalculer un `Summary` de manière déterministe.

**Attributs**

- **minute** — *integer* — minute écoulée depuis le début du match
- **second** — *integer* — seconde dans la minute

**Invariants**

- `minute` doit être un entier **≥ 0**
- `second` doit être un entier **entre 0 et 59**
- Le timestamp doit être **cohérent avec le match** (invariant porté par `Match`) :
  - il ne doit pas dépasser la durée maximale du match si cette durée est définie,
  - il doit être compatible avec la période courante si le match est structuré en mi-temps/périodes.

**Format JSON attendu**

- **Type** : `object`

- **Schéma** : `tests/schemas/event-timestamp.schema.json`
- **Fixture valide** : `tests/fixtures/event-timestamp.valid.json`
- **Fixture invalide** : `tests/fixtures/event-timestamp.invalid.json`

**Tests minimaux attendus**

- Accepter :
  - `{ "minute": 0, "second": 0 }`
  - `{ "minute": 3, "second": 12 }`
- Rejeter :
  - `{ "minute": -1, "second": 0 }`
  - `{ "minute": 0, "second": -1 }`
  - `{ "minute": 0, "second": 60 }`
  - types invalides (string, float, null, champs absents)

**Génération des fixtures**

- Générer `event-timestamp.valid.json` avec une valeur simple et valide (ex : `minute=3`, `second=12`).
- Générer `event-timestamp.invalid.json` avec une violation des invariants (ex : `second=60` ou `minute=-1`).