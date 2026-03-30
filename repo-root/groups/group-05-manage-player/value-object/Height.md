# Height

**Résumé métier**

Représente la taille d’un joueur exprimée en centimètres.

**Attributs**

- value - *Number*

**Invariants**

- value doit être non vide
- value doit être un nombre
- value doit être strictement positive
- value doit être exprimée en centimètres

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Height.schema.json`
- **Fixture valide** : `tests/fixtures/Height.valid.json`
- **Fixture invalide** : `tests/fixtures/Height.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’une height valide est acceptée
- Vérifier qu’une height vide est rejetée
- Vérifier qu’une height égale à 0 est rejetée
- Vérifier qu’une height négative est rejetée
- Vérifier qu’une height non numérique est rejetée