# LastName

**Résumé métier**

Représente le nom d’un joueur.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value ne doit pas contenir uniquement des espaces
- value doit avoir une longueur comprise entre 1 et 100 caractères

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/LastName.schema.json`
- **Fixture valide** : `tests/fixtures/LastName.valid.json`
- **Fixture invalide** : `tests/fixtures/LastName.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un lastName valide est accepté
- Vérifier qu’un lastName vide est rejeté
- Vérifier qu’un lastName contenant uniquement des espaces est rejeté
- Vérifier qu’un lastName trop long est rejeté
- Vérifier qu’un lastName non string est rejeté