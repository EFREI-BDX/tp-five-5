# Phone

**Résumé métier**

Représente le numéro de téléphone d’un joueur.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit respecter un format téléphone valide
- value ne doit pas contenir uniquement des espaces

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Phone.schema.json`
- **Fixture valide** : `tests/fixtures/Phone.valid.json`
- **Fixture invalide** : `tests/fixtures/Phone.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un phone valide est accepté
- Vérifier qu’un phone vide est rejeté
- Vérifier qu’un phone invalide est rejeté
- Vérifier qu’un phone contenant uniquement des espaces est rejeté
- Vérifier qu’un phone non string est rejeté