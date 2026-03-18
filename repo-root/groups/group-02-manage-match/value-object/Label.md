# Label

**Résumé métier**
Label d'une entité.

**Attributs**

- **value** — *string*

**Invariants**

- **value doit être non vide**

**Format JSON attendu**

- **Schéma** : `tests/schemas/Label.schema.json`
- **Fixture valide** : `tests/fixtures/Label.valid.json`
- **Fixture invalide** : `tests/fixtures/Label.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec une value non vide ne lève pas d'exception.
- **createInvalidThrows** — value vide lève une exception métier.
- **jsonRoundtrip** — sérialisation/désérialisation conserve la valeur.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
