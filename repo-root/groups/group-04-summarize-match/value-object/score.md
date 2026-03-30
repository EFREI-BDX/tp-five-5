# Score

**Résumé métier**  
Représente le score final d’un match de Five : nombre de buts de l’équipe domicile et de l’équipe extérieure.

**Attributs**

- **homeGoals** — *integer* — nombre de buts équipe domicile
- **awayGoals** — *integer* — nombre de buts équipe extérieure

**Invariants**

- `homeGoals` doit être un entier.
- `awayGoals` doit être un entier.
- `homeGoals` doit être ≥ 0.
- `awayGoals` doit être ≥ 0.
- Le score doit être cohérent avec le match :
  - Si le match n’est pas terminé (`Finished`) ou déclaré forfait (`Forfeited`), le score final ne doit pas être “publié” (si le workflow distingue draft/published).
- En cas de forfait (`Forfeited`), le score peut être un **score administratif** (règle à préciser), et la politique de calcul des statistiques doit être explicitée dans le domaine.

**Format JSON attendu**

- **Schéma** : `tests/schemas/score.schema.json`
- **Fixture valide** : `tests/fixtures/score.valid.json`
- **Fixture invalide** : `tests/fixtures/score.invalid.json`

**Tests minimaux attendus**

- Accepter :
  - `homeGoals = 0` et `awayGoals = 0`
  - `homeGoals = 3` et `awayGoals = 2`
- Rejeter :
  - `homeGoals < 0` ou `awayGoals < 0`
  - `homeGoals` ou `awayGoals` non entier (`"2"`, `2.5`, `null`)
  - champ manquant

**Génération des fixtures**

- Fixture valide : générer deux entiers ≥ 0 (ex : `2` et `1`).
- Fixture invalide : générer au moins un cas invalide (ex : `homeGoals = -1` ou `awayGoals = "x"`).