# Period

**Résumé métier**
Intervalle de temps entre le début et la fin du match

**Attributs**

- **date** - *Date*
- **start** — *Heure*
- **end** — *Heure* — contrainte : Après le start

**Invariants**

- **date + start doivent être dans le futur**
- **end doit être après start**
- **Tous les attributs sont obligatoires**

**Format JSON attendu**

- **Schéma** : `tests/schemas/Period.schema.json`
- **Fixture valide** : `tests/fixtures/Period.valid.json`
- **Fixture invalide** : `tests/fixtures/Period.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec des valeur respectant les contraintes.
- **createInvalidThrows** — valeur ne respectant pas les contraintes lève une exception métier.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
