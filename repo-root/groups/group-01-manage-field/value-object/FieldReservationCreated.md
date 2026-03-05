# FieldReservationCreated

**Resume metier**
Evenement emis lors de la creation d'une reservation de terrain.

**Attributs**

- **reservationId** - *string* (uuid)
- **fieldId** - *string* (uuid)
- **slot** - *TimeSlot*

**Invariants**

- **reservationId** doit etre un UUID valide.
- **fieldId** doit etre un UUID valide.
- **slot** doit respecter le schema `TimeSlot`.
- **slot.endAt > slot.startAt** (regle metier).

**Format JSON attendu**

- **Schema** : `tests/schemas/field-reservation-created.schema.json`
- **Fixture valide** : `tests/fixtures/field-reservation-created.valid.json`
- **Fixture invalide (schema)** : `tests/fixtures/field-reservation-created.missing-slot.invalid.json`
- **Fixture invalide (metier)** : `tests/fixtures/field-reservation-created.slot.same-start-end.business-rule.invalid.json`

**Tests minimaux attendus**

- **createValid** - payload valide ne leve pas d'exception.
- **createInvalidThrows** - uuid ou slot invalide leve une exception metier.
- **jsonRoundtrip** - serialisation/deserialisation conserve les valeurs.
- **schemaValidation** - fixture schema invalide echoue; fixture metier invalide est rejetee par le domaine.

**Generation des fixtures**

- Documenter la commande ou le test qui produit les fixtures.
