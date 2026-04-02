# Team

**Résumé métier**

**Attributs**

- id — *VO Id*
- label — *VO Label*
- tag - *VO Tag*
- players - *List of Player*
- period - *VO Period*
- teamLeader - *Player*
- state - *VO TeamState*

**Invariants**

- id doit être non vide
- label doit être non vide
- tag doit être non vide
- players doit comporter au maximum 8 joueurs
- creationDate doit être non vide
- dissolutionDate si elle est renseignée doit être supérieure ou égale à creationDate
- teamLeader si non renseigné, est choisi au hasard parmi la liste des joueurs de l'équipe
- teamLeader doit faire partie de players
- state doit être non vide
- state doit être égal à dissoute si dissolutionDate est renseignée
- state doit être égal à incomplète si players contient moins de 5 joueurs
- state doit être égal à active si players contient au moins 5 joueurs et dissolutionDate n'est pas renseignée

**Format JSON attendu**

- **Schéma** : `tests/schemas/Team.schema.json`
- **Fixture valide** : `tests/fixtures/Team.valid.json`
- **Fixture invalide** : `tests/fixtures/Team.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**
