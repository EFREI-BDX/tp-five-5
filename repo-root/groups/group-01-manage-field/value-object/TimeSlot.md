# TimeSlot

**Resume metier**
Creneau temporel de reservation d'un terrain.

**Attributs**

- **startAt** - *string* (date-time ISO-8601)
- **endAt** - *string* (date-time ISO-8601)

**Invariants**

- **startAt** et **endAt** doivent etre au format date-time ISO-8601.
- **endAt > startAt** (regle metier, verifiee dans le domaine).

**Format JSON attendu**

- **Schema** : `tests/schemas/time-slot.schema.json`
- **Fixture valide** : `tests/fixtures/time-slot.valid.json`
- **Fixture invalide (schema)** : `tests/fixtures/time-slot.invalid.start-at-format.json`
- **Fixture invalide (metier)** : `tests/fixtures/time-slot.same-start-end.business-rule.invalid.json`
- **Fixture invalide (metier)** : `tests/fixtures/time-slot.end-before-start.business-rule.invalid.json`

**Tests minimaux attendus**

- **createValid** - creneau valide ne leve pas d'exception.
- **createInvalidThrows** - format invalide ou `endAt <= startAt` leve une exception metier.
- **equalityByValue** - deux creneaux identiques sont egaux.
- **jsonRoundtrip** - serialisation/deserialisation conserve les valeurs.
- **schemaValidation** - fixture schema invalide echoue; fixture metier invalide est rejetee par le domaine.

**Generation des fixtures**

- Documenter la commande ou le test qui produit les fixtures.
