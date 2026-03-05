# FieldStatusChanged

**Resume metier**
Evenement emis lors d'un changement de statut d'un terrain.

**Attributs**

- **fieldId** - *string* (uuid)
- **previousStatus** - *string* (enum: `active`, `maintenance`, `unavailable`)
- **newStatus** - *string* (enum: `active`, `maintenance`, `unavailable`)
- **reason** - *string* optionnel (minLength: 1)

**Invariants**

- **fieldId** doit etre un UUID valide.
- **previousStatus** et **newStatus** doivent etre valides.
- **previousStatus** et **newStatus** doivent etre differents (regle metier).
- si **reason** est present, il doit etre non vide.

**Format JSON attendu**

- **Schema** : `tests/schemas/field-status-changed.schema.json`
- **Fixture valide** : `tests/fixtures/field-status-changed.valid.json`
- **Fixture invalide (schema)** : `tests/fixtures/field-status-changed.new-status-enum.invalid.json`
- **Fixture invalide (metier)** : `tests/fixtures/field-status-changed.same-status.business-rule.invalid.json`

**Tests minimaux attendus**

- **createValid** - payload valide ne leve pas d'exception.
- **createInvalidThrows** - enum invalide, statuts identiques ou reason invalide levent une exception metier.
- **jsonRoundtrip** - serialisation/deserialisation conserve les valeurs.
- **schemaValidation** - fixture schema invalide echoue; fixture metier invalide est rejetee par le domaine.

**Generation des fixtures**

- Documenter la commande ou le test qui produit les fixtures.
