# TeamState

**Résumé métier**
Statut d'une équipe.

**Attributs**

- **value** — *string* — contrainte :  — valeur autorisée : **INCOMPLETE, COMPLETE, DISSOUTE**

**Invariants**

- **value** doit appartenir à l'énumération autorisée.
- **value doit être non vide**

**Format JSON attendu**

- **Schéma** : `tests/schemas/TeamState.schema.json`
- **Fixture valide** : `tests/fixtures/TeamState.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamState.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec une valeur d'énumération valide ne lève pas d'exception.
- **createInvalidThrows** — valeur hors énumération lève une exception métier.
- **jsonRoundtrip** — sérialisation/désérialisation conserve la valeur.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
