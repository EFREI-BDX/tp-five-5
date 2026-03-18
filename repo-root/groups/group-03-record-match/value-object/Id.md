# Id

**Résumé métier**
Identifiant unique universel d'une entité du domaine (Match, Event, Player, Team).

**Attributs**

- **value** - *UUID* - chaîne au format UUID v4

**Invariants**

- **value** doit respecter le format UUID (`xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx`)
- **value** doit être non vide

**Format JSON attendu**

- **Schéma** : `tests/schemas/id.schema.json`
- **Fixture valide** : `tests/fixtures/id.valid.json`
- **Fixture invalide** : `tests/fixtures/id.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un UUID valide ne lève pas d'exception.
- **createInvalidThrows** - chaîne non UUID lève une exception métier.
- **equalityByValue** - deux instances avec le même UUID sont égales.
- **jsonRoundtrip** - sérialisation/désérialisation conserve la valeur.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
