
# Tag

**Résumé métier**
Donnée de trois lettres maximale identifiant une équipe.

**Attributs**

- **value** — *string*

**Invariants**

- **value doit avoir 3 lettres au maximum**
- **velue doit être non vide**

**Format JSON attendu**

- **Schéma** : `tests/schemas/Tag.schema.json`
- **Fixture valide** : `tests/fixtures/Tag.valid.json`
- **Fixture invalide** : `tests/fixtures/Tag.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec une valeur <= 3 caractères ne lève pas d'exception.
- **createInvalidThrows** — valeur > 3 caractères lève une exception métier.
- **jsonRoundtrip** — sérialisation/désérialisation conserve la valeur.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
