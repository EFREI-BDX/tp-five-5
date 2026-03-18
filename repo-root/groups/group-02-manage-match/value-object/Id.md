# Id

**Résumé métier**
Identifiant qui identifie les entités.

**Attributs**

- **value** — *int*

**Invariants**

- **value doit être non vide**

**Format JSON attendu**

- **Schéma** : `tests/schemas/Id.schema.json`
- **Fixture valide** : `tests/fixtures/Id.valid.json`
- **Fixture invalide** : `tests/fixtures/Id.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec une value non vide ne lève pas d'exception.
- **createInvalidThrows** — value vide lève une exception métier.
- **jsonRoundtrip** — sérialisation/désérialisation conserve la valeur.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
