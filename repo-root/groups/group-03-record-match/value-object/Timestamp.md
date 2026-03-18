# Timestamp

**Résumé métier**
Horodatage d'un événement survenu pendant un match, exprimé au format ISO-8601.

**Attributs**

- **value** - *string* - datetime au format ISO-8601 (ex. `2025-06-15T18:45:12Z`)

**Invariants**

- **value** doit respecter le format datetime ISO-8601
- **value** doit être non vide

**Format JSON attendu**

- **Schéma** : `tests/schemas/timestamp.schema.json`
- **Fixture valide** : `tests/fixtures/timestamp.valid.json`
- **Fixture invalide** : `tests/fixtures/timestamp.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un datetime ISO-8601 valide ne lève pas d'exception.
- **createInvalidThrows** - chaîne non conforme au format ISO-8601 lève une exception métier.
- **equalityByValue** - deux instances avec la même valeur sont égales.
- **jsonRoundtrip** - sérialisation/désérialisation conserve la valeur.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
