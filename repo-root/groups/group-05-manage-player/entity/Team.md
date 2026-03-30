# Team

**Résumé métier**

Permettre la gestion des équipes dans le système ERP du centre de foot à 5.

**Attributs**

- id - *VO TeamId*
- name - *VO TeamName*

**Invariants**

- id doit être non vide
- name doit être non vide

**Format JSON attendus**

- **Schéma JSON** : `tests/schemas/Team.schema.json`
- **Fixture valide** : `tests/fixtures/Team.valid.json`
- **Fixture invalide** : `tests/fixtures/Team.invalid.json`

**Tests minimaux attendus**

- Vérifier qu'une team valide est acceptée
- Vérifier qu'un id vide est rejeté
- Vérifier qu'un name vide est rejeté
