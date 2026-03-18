**Résumé métier** *(À compléter : Description métier de ce que représente un Match dans votre domaine)*

**Attributs**

- id — *VO Id*
- period — *VO Period*
- matchState — *VO MatchState*
- teamA - *Team*
- teamB - *Team*
- field - *Field*

**Invariants**

- id doit être non vide
- period doit être non vide
- statut doit être non vide
- teamA et teamB doivent être non vide.
- field doit être non vide.
- matchState est dynamique en fonction de la période si on est avant, c'est planned si on est pendant, ça doit être 
in_progress, si on est après ça doit être completed s'il à été annulé ça passe en cancelled.

**Format JSON attendu**

- **Schéma** : `tests/schemas/Match.schema.json`
- **Fixture valide** : `tests/fixtures/Match.valid.json`
- **Fixture invalide** : `tests/fixtures/Match.invalid.json`

**Tests minimaux attendus**

**Génération des fixtures**