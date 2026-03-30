# Email

**Résumé métier**

Représente l’adresse email d’un joueur.

**Attributs**

- value - *String*

**Invariants**

- value doit être non vide
- value doit être une chaîne de caractères
- value doit respecter un format email valide
- value ne doit pas contenir uniquement des espaces

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Email.schema.json`
- **Fixture valide** : `tests/fixtures/Email.valid.json`
- **Fixture invalide** : `tests/fixtures/Email.invalid.json`

**Tests minimaux attendus**

- Vérifier qu’un email valide est accepté
- Vérifier qu’un email vide est rejeté
- Vérifier qu’un email invalide est rejeté
- Vérifier qu’un email contenant uniquement des espaces est rejeté
- Vérifier qu’un email non string est rejeté