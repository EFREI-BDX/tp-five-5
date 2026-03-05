# ItemStats

**Résumé métier**  
Ensemble d'attributs numériques décrivant les capacités d'un item (attaque, défense, poids).

**Attributs**
- **attack** — *integer* — contrainte : **>= 0**
- **defense** — *integer* — contrainte : **>= 0**
- **weight** — *number* — contrainte : **>= 0**

**Invariants**
- **attack >= 0**
- **defense >= 0**
- **weight >= 0**

**Format JSON attendu**
- **Schéma** : `tests/schemas/item-stats.schema.json`
- **Fixture valide** : `tests/fixtures/item-stats.valid.json`
- **Fixture invalide** : `tests/fixtures/item-stats.invalid.json`

**Tests minimaux attendus**
- **createValid** — construction avec valeurs non négatives ne lève pas d'exception.
- **createInvalidThrows** — toute valeur négative lève une exception métier.
- **equalityByValue** — deux instances avec mêmes valeurs sont égales.
- **jsonRoundtrip** — sérialisation/désérialisation conserve les valeurs.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**
- Indiquer la commande ou le test qui produit les fixtures.
