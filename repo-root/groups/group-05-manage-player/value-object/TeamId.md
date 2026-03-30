# TeamId

**Résumé métier**

Représente l’identifiant unique d’une équipe à laquelle un joueur a participé.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value ne doit pas contenir uniquement des espaces

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/TeamId.schema.json`
- **Fixture valide** : `tests/fixtures/TeamId.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamId.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un teamId valide est accepté
- Vérifier qu’un teamId vide est rejeté
- Vérifier qu’un teamId contenant uniquement des espaces est rejeté
- Vérifier qu’un teamId non string est rejeté