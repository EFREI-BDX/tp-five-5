# TeamName

**Résumé métier**

Représente le nom d'une équipe.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value ne doit pas contenir uniquement des espaces
- value doit avoir une longueur comprise entre 1 et 100 caractères

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/TeamName.schema.json`
- **Fixture valide** : `tests/fixtures/TeamName.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamName.invalid.json`

**Tests minimaux attendus**

- Vérifier qu'un teamName valide est accepté
- Vérifier qu'un teamName vide est rejeté
- Vérifier qu'un teamName contenant uniquement des espaces est rejeté
- Vérifier qu'un teamName trop long est rejeté
- Vérifier qu'un teamName non string est rejeté
