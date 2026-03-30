# Status

**Résumé métier**

Représente l’état métier d’un joueur dans le système.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit être égal à `actif`, `inactif`, `supprimé`

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Status.schema.json`
- **Fixture valide** : `tests/fixtures/Status.valid.json`
- **Fixture invalide** : `tests/fixtures/Status.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un status valide est accepté
- Vérifier qu’un status vide est rejeté
- Vérifier qu’un status hors enum est rejeté
- Vérifier qu’un status non string est rejeté