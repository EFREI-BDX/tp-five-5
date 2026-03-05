# TimeSlot

**Resume metier**
Creneau temporel de reservation d'un terrain.

**Attributs**

- **startAt** - *string* (date-time ISO-8601)
- **endAt** - *string* (date-time ISO-8601)

**Invariants**

- **startAt** et **endAt** doivent etre au format date-time ISO-8601.
- **endAt > startAt** (verifie dans le domaine applicatif).

**Format JSON attendu**

- **Schema** : `tests/schemas/time-slot.schema.json`
- **Fixture valide** : `tests/fixtures/time-slot.valid.json`
- **Fixture invalide** : `tests/fixtures/time-slot.invalid.end-at-type.json`

**Tests minimaux attendus**

- **createValid** - creneau valide ne leve pas d'exception.
- **createInvalidThrows** - format invalide ou `endAt <= startAt` leve une exception metier.
- **equalityByValue** - deux creneaux identiques sont egaux.
- **jsonRoundtrip** - serialisation/deserialisation conserve les valeurs.
- **schemaValidation** - fixture valide valide le schema; fixture invalide echoue.

**Generation des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
