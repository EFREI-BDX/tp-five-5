# FieldDeleted

**Resume metier**
Evenement emis a la suppression d'un terrain.

**Attributs**

- **fieldId** - *string* (uuid)

**Invariants**

- **fieldId** doit etre un UUID valide.

**Format JSON attendu**

- **Schema** : `tests/schemas/field-deleted.schema.json`
- **Fixture valide** : `tests/fixtures/field-deleted.valid.json`
- **Fixture invalide** : `tests/fixtures/field-deleted.field-id-format.invalid.json`

**Tests minimaux attendus**

- **createValid** - payload valide ne leve pas d'exception.
- **createInvalidThrows** - `fieldId` invalide leve une exception metier.
- **jsonRoundtrip** - serialisation/deserialisation conserve la valeur.
- **schemaValidation** - fixture valide passe le schema; fixture invalide echoue.

**Generation des fixtures**

- Documenter la commande ou le test qui produit les fixtures.
