# FieldDimensions

**Resume metier**
Dimensions d'un terrain de five en metres.

**Attributs**

- **lengthMeters** - *integer* - contrainte: **>= 25 et <= 50**
- **widthMeters** - *integer* - contrainte: **>= 15 et <= 35**

**Invariants**

- **25 <= lengthMeters <= 50**
- **15 <= widthMeters <= 35**
- **lengthMeters > widthMeters**

**Format JSON attendu**

- **Schema** : `tests/schemas/field-dimensions.schema.json`
- **Fixture valide** : `tests/fixtures/field-dimensions.valid.json`
- **Fixture invalide** : `tests/fixtures/field-dimensions.invalid.length-too-short.json`

**Tests minimaux attendus**

- **createValid** - construction avec dimensions valides ne leve pas d'exception.
- **createInvalidThrows** - longueur/largeur hors bornes leve une exception metier.
- **equalityByValue** - deux instances identiques sont egales.
- **jsonRoundtrip** - serialisation/deserialisation conserve les valeurs.
- **schemaValidation** - fixture valide valide le schema; fixture invalide echoue.

**Generation des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
