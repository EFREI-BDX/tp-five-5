# MatchState

**Résumé métier**
Statut d'un match.

**Attributs**

- **value** — *string* — contrainte :  — valeur autorisée : **NOT_STARTED, IN_PROGRESS, FINISHED, CANCELLED**

**Invariants**

- **value** doit appartenir à l'énumération autorisée.

**Format JSON attendu**

- **Schéma** : `tests/schemas/MatchState.schema.json`
- **Fixture valide** : `tests/fixtures/MatchState.valid.json`
- **Fixture invalide** : `tests/fixtures/MatchState.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec une valeur d'énumération valide ne lève pas d'exception.
- **createInvalidThrows** — valeur hors énumération lève une exception métier.
- **jsonRoundtrip** — sérialisation/désérialisation conserve la valeur.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**

- Indiquer la commande ou le test qui produit les fixtures.
