# Team

**Résumé métier**

**Attributs**

- id — *VO Id*
- name — *VO Label*
- tag - *VO Tag*
- players - *List of Player*
- creationDate - *VO CreationDate*
- dissolutionDate - *VO DissolutionDate*
- teamLeader - *Player*

**Invariants**

- id doit être non vide
- name doit être non vide
- tag doit être non vide
- players doit comporter au moins 5 joueurs
- creationDate doit être non vide
- dissolutionDate doit être supérieure ou égale à creationDate si elle est renseignée
- teamLeader doit être non vide
- teamLeader doit faire partie de players

**Format JSON attendu**

- **Schéma** : `tests/schemas/Team.schema.json`
- **Fixture valide** : `tests/fixtures/Team.valid.json`
- **Fixture invalide** : `tests/fixtures/Team.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**
