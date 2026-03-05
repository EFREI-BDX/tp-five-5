# FieldCreated

**Resume metier**
Evenement emis a la creation d'un terrain.

**Attributs**

- **fieldId** - *string* (uuid)
- **name** - *string* (minLength: 1)
- **status** - *string* (enum: `active`, `maintenance`, `unavailable`)

**Invariants**

- **fieldId** doit etre un UUID valide.
- **name** ne doit pas etre vide.
- **status** doit appartenir a l'enumeration autorisee.
- **name** ne doit pas etre compose uniquement d'espaces (regle metier).

**Format JSON attendu**

- **Schema** : `tests/schemas/field-created.schema.json`
- **Fixture valide** : `tests/fixtures/field-created.valid.json`
- **Fixture invalide (schema)** : `tests/fixtures/field-created.status-enum.invalid.json`
- **Fixture invalide (metier)** : `tests/fixtures/field-created.name-whitespace-only.business-rule.invalid.json`

**Tests minimaux attendus**

- **createValid** - payload valide ne leve pas d'exception.
- **createInvalidThrows** - uuid/status/name invalides levent une exception metier.
- **jsonRoundtrip** - serialisation/deserialisation conserve les valeurs.
- **schemaValidation** - fixture schema invalide echoue; fixture metier invalide est rejetee par le domaine.

**Generation des fixtures**

- Documenter la commande ou le test qui produit les fixtures.
