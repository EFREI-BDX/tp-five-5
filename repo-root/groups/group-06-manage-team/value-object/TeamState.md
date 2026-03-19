# TeamState

**Résumé métier**
Une chaine de caractères qui correspond à l'état d'une équipe, qui peut être "Active", "Incomplète" ou "Dissoute".

**Attributs**

- value — *String*

**Invariants**

- value doit être non vide
- value doit être égal à "Active", "Incomplète" ou "Dissoute"

**Format JSON attendu**

- **Schéma** : `tests/schemas/TeamState.schema.json`
- **Fixture valide** : `tests/fixtures/TeamState.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamState.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**
