# Quantity

**Résumé métier**  
Nombre d'exemplaires d'un item détenus dans l'inventaire.

**Attributs**

- **quantity** — *integer* — contrainte : **>= 1**

**Invariants**

- **quantity >= 1**

**Format JSON attendu**

- **Schéma** : `tests/schemas/quantity.schema.json`
- **Fixture valide** : `tests/fixtures/quantity.valid.json`
- **Fixture invalide** : `tests/fixtures/quantity.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec `quantity >= 1` ne lève pas d'exception.
- **createInvalidThrows** — `quantity <= 0` lève une exception métier (ex. `IllegalArgumentException`).
- **jsonRoundtrip** — sérialisation puis désérialisation conserve la valeur.
- **schemaValidation** — la fixture valide passe la validation JSON Schema ; la fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
