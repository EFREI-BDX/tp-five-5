# Name

**Résumé métier**
Une chaine de caractères qui peut servir de dénomination.

**Attributs**

- firstName — *String*
- lastName — *String*

**Invariants**

- firstName doit être non vide
- lastName doit être non vide
- firstName et lastName doivent commencer par une majuscule
- firstName et lastName doivent être composés de lettres uniquement et peuvent contenir des espaces, des tirets ou des
  apostrophes

**Format JSON attendu**

- **Schéma** : `tests/schemas/Name.schema.json`
- **Fixture valide** : `tests/fixtures/Name.valid.json`
- **Fixture invalide** : `tests/fixtures/Name.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**
