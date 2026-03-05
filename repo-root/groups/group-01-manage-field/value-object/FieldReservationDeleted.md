# FieldReservationDeleted

**Resume metier**
Evenement emis lors de la suppression d'une reservation de terrain.

**Attributs**

- **reservationId** - *string* (uuid)
- **fieldId** - *string* (uuid)

**Invariants**

- **reservationId** doit etre un UUID valide.
- **fieldId** doit etre un UUID valide.

**Format JSON attendu**

- **Schema** : `tests/schemas/field-reservation-deleted.schema.json`
- **Fixture valide** : `tests/fixtures/field-reservation-deleted.valid.json`
- **Fixture invalide** : `tests/fixtures/field-reservation-deleted.missing-reservation-id.invalid.json`

**Tests minimaux attendus**

- **createValid** - payload valide ne leve pas d'exception.
- **createInvalidThrows** - identifiants invalides/absents levent une exception metier.
- **jsonRoundtrip** - serialisation/deserialisation conserve les valeurs.
- **schemaValidation** - fixture valide passe le schema; fixture invalide echoue.

**Generation des fixtures**

- Documenter la commande ou le test qui produit les fixtures.
