# Action

**Résumé métier**
Type d'un événement survenu pendant un match de five. Appartient à une énumération fixe de six actions possibles.

**Attributs**

- **value** - *string* - valeur autorisée : **goal, foul, yellowCard, redCard, kickOff, endMatch**

**Invariants**

- **value** doit appartenir à l'énumération autorisée
- **value** doit être non vide

**Format JSON attendu**

- **Schéma** : `tests/schemas/action.schema.json`
- **Fixture valide** : `tests/fixtures/action.valid.json`
- **Fixture invalide** : `tests/fixtures/action.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec une valeur d'énumération valide ne lève pas d'exception.
- **createInvalidThrows** - valeur hors énumération lève une exception métier.
- **equalityByValue** - deux instances avec la même valeur sont égales.
- **jsonRoundtrip** - sérialisation/désérialisation conserve la valeur.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.
