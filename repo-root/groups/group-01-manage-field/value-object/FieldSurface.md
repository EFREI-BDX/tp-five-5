# FieldSurface

**Resume metier**
Type de surface d'un terrain indoor.

**Attributs**

- **surface** - *string* - valeur autorisee: **synthetic_turf, parquet, concrete, hybrid_turf**

**Invariants**

- **surface** doit appartenir a l'enumeration autorisee.

**Format JSON attendu**

- **Schema** : `tests/schemas/field-surface.schema.json`
- **Fixture valide** : `tests/fixtures/field-surface.valid.json`
- **Fixture invalide** : `tests/fixtures/field-surface.invalid.json`

**Tests minimaux attendus**

- **createValid** - valeur valide ne leve pas d'exception.
- **createInvalidThrows** - valeur hors enum leve une exception metier.
- **jsonRoundtrip** - serialisation/deserialisation conserve la valeur.
- **schemaValidation** - fixture valide valide le schema; fixture invalide echoue.

**Generation des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
