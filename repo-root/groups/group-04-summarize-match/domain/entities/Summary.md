# Summary

**Résumé métier**

Représente le résumé calculé d’un match de Five.  
Il regroupe le score final, les statistiques (équipes/joueurs) et les récompenses (awards). Un `Summary` est rattaché à un seul `Match`.

**Attributs**

- **matchId** — *VO MatchId* — identifiant du match résumé
- **matchStatus** — *MatchStatus* — statut du match au moment du résumé (`Finished`, `Forfeited`, `Cancelled`, etc.)
- **score** — *VO Score* — score final (domicile/extérieur)

- **teamStats** — *TeamStats[]* — statistiques par équipe (une entrée par équipe du match)
  - **teamId** — *VO TeamId*
  - **goals** — *integer*
  - **shots** — *integer*
  - **shotsOnTarget** — *integer*
  - **passes** — *integer*
  - **passesSucceeded** — *integer*
  - **fouls** — *integer*
  - **yellowCards** — *integer*
  - **redCards** — *integer*
  - **goalsConceded** — *integer*
  - **cleanSheet** — *boolean*
  - **possession** — *number* (optionnel, selon données disponibles)

- **playerStats** — *PlayerStats[]* — statistiques par joueur (une entrée par joueur du match)
  - **playerId** — *VO PlayerId*
  - **teamId** — *VO TeamId*
  - **goals** — *integer*
  - **assists** — *integer*
  - **shots** — *integer*
  - **shotsOnTarget** — *integer*
  - **passes** — *integer*
  - **passesSucceeded** — *integer*
  - **fouls** — *integer*
  - **yellowCards** — *integer*
  - **redCards** — *integer*
  - **saves** — *integer* (optionnel, selon données disponibles)
  - **goalsConceded** — *integer* (optionnel, selon règles gardien)
  - **playTimeSeconds** — *integer* (optionnel, selon substitutions/horodatage)
  - **rate** — *PlayerRate* (optionnel, si la note est dans le scope)

- **awards** — *Awards* — récompenses calculées
  - **bestScorer** — *VO PlayerId | PlayerId[] | null*
  - **bestGoalkeeper** — *VO PlayerId | PlayerId[] | null*
  - **bestDefender** — *VO PlayerId | PlayerId[] | null*
  - **mvp** — *VO PlayerId | null*

- **forfeit** — *Forfeit* (optionnel) — présent uniquement si `matchStatus = Forfeited`
- **anomalies** — *string[]* (optionnel) — liste de problèmes détectés (données incohérentes/incomplètes)
- **generatedAt** — *string* (date-time, optionnel) — date de génération du résumé
- **version** — *integer* (optionnel) — version des règles de calcul

**Invariants**

- `matchId` doit être renseigné et valide.
- `score.homeGoals` et `score.awayGoals` doivent être des entiers **≥ 0**.
- `teamStats` doit contenir **exactement 2** entrées (une par équipe du match) et chaque `teamId` doit être unique.
- Pour chaque entrée de `teamStats` :
  - `shotsOnTarget` ≤ `shots`
  - `passesSucceeded` ≤ `passes`
  - `goals`, `shots`, `shotsOnTarget`, `passes`, `passesSucceeded`, `fouls`, `yellowCards`, `redCards`, `goalsConceded` sont des entiers **≥ 0**
- `cleanSheet = true` implique `goalsConceded = 0`.
- `playerStats` ne doit contenir qu’une seule entrée par `playerId`.
- Pour chaque entrée de `playerStats` :
  - `shotsOnTarget` ≤ `shots`
  - `passesSucceeded` ≤ `passes`
  - les compteurs sont des entiers **≥ 0**
- `BestScorer` peut être vide (`null` ou liste vide selon modèle).
- Si `bestScorer` est défini (ou liste non vide) :
  - le(s) joueur(s) référencé(s) existe(nt) dans `playerStats`
  - il(s) a/ont strictement plus de 0 but
  - il(s) fait/font partie des joueurs ayant le maximum de buts
- Si `matchStatus = Forfeited` :
  - `forfeit` doit être présent
  - la règle de score administratif (ou le score administratif) doit être déterminée
  - la politique sur les statistiques (ignorées / conservées / non officielles) doit être explicite (au moins via `anomalies` ou un champ dédié)

**Format JSON attendu**

- **Schéma** : `tests/schemas/summary.schema.json`
- **Fixture valide** : `tests/fixtures/summary.valid.json`
- **Fixture invalide** : `tests/fixtures/summary.invalid.json`

**Tests minimaux attendus**

- Doit accepter un `Summary` avec :
  - `matchId` valide
  - `score` valide (0–0 autorisé)
  - `teamStats` avec 2 équipes et compteurs cohérents
  - `playerStats` cohérents
- Doit rejeter un `Summary` si :
  - `matchId` est vide / absent
  - un compteur est négatif
  - `shotsOnTarget > shots` ou `passesSucceeded > passes`
  - `teamStats` n’a pas exactement 2 équipes ou contient des `teamId` dupliqués
  - `bestScorer` est défini alors que le joueur a 0 but

**Génération des fixtures**

- `summary.valid.json` : construire un exemple minimal cohérent (match terminé, 2 équipes, score et compteurs alignés).
- `summary.invalid.json` : construire un exemple contenant au moins une violation d’invariant (ex : `shotsOnTarget > shots` ou `homeGoals = -1`).