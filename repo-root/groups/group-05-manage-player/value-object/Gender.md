# Gender

**Résumé métier**

Représente le genre déclaré d’un joueur.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit être égal à `homme`, `femme`, `non binaire`, `non spécifié`

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Gender.schema.json`
- **Fixture valide** : `tests/fixtures/Gender.valid.json`
- **Fixture invalide** : `tests/fixtures/Gender.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un gender valide est accepté
- Vérifier qu’un gender vide est rejeté
- Vérifier qu’un gender hors enum est rejeté
- Vérifier qu’un gender non string est rejeté