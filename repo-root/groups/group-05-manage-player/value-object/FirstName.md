# Id

**Résumé métier**

Représente l’identifiant unique d’un joueur dans le système ERP du centre de foot à 5.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit être unique dans le contexte des joueurs
- value ne doit pas contenir uniquement des espaces

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Id.schema.json`
- **Fixture valide** : `tests/fixtures/Id.valid.json`
- **Fixture invalide** : `tests/fixtures/Id.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un id valide est accepté
- Vérifier qu’un id vide est rejeté
- Vérifier qu’un id contenant uniquement des espaces est rejeté
- Vérifier qu’un id non string est rejeté