# StartTime

**Résumé métier**
Heure de début d'un match de five, exprimée au format ISO-8601.

**Attributs**

- **value** - *string* - datetime au format ISO-8601 (ex. `2025-06-15T18:30:00Z`)

**Invariants**

- **value** doit respecter le format datetime ISO-8601
- **value** doit être non vide

**Format JSON attendu**

- **Schéma** : `tests/schemas/start-time.schema.json`
- **Fixture valide** : `tests/fixtures/start-time.valid.json`
- **Fixture invalide** : `tests/fixtures/start-time.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un datetime ISO-8601 valide ne lève pas d'exception.
- **createInvalidThrows** - chaîne non conforme au format ISO-8601 lève une exception métier.
- **equalityByValue** - deux instances avec la même valeur sont égales.
- **jsonRoundtrip** - sérialisation/désérialisation conserve la valeur.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
