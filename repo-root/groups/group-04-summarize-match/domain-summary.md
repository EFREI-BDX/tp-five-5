# Domain Summary — Summarize Match

**Objectif métier**

Générer un résumé de match de Five (5 vs 5) à partir des évènements de match afin de produire le score final, les statistiques (équipes/joueurs) et les récompenses (awards), y compris en cas de forfait.

**Ubiquitous language**

- **Match** : une rencontre de five entre deux équipes (domicile/extérieur), décrite par un statut, des participants et une timeline d’évènements.
- **Team** : une équipe qui participe au match. En five, une équipe aligne 5 joueurs sur le terrain simultanément.
- **Player** : un joueur participant au match via une équipe.
- **TeamLeader** : un joueur désigné comme leader/organisateur de l’équipe (rôle de coordination, hors performance sportive).
- **Event** : un fait de jeu horodaté appartenant à un match (but, faute, carton, tir, passe, substitution…).
- **Timeline** : la liste ordonnée des évènements d’un match.
- **Summary** : le résultat calculé du match (score final, statistiques, awards, faits marquants/anomalies éventuelles).
- **Score** : le score final du match (buts équipe domicile / buts équipe extérieure).
- **Goal** : un but marqué par une équipe, attribué à un joueur (buteur) et à un timestamp.
- **Assist** : la passe décisive associée à un but (optionnelle).
- **Shot** : une tentative de tir.
- **ShotOnTarget** : un tir cadré (peut être un type d’event séparé ou un attribut du tir selon le modèle retenu).
- **Save** : un arrêt du gardien empêchant un but.
- **Foul** : une faute commise par un joueur.
- **YellowCard** : un carton jaune attribué à un joueur.
- **RedCard** : un carton rouge attribué à un joueur (expulsion).
- **Substitution** : un remplacement (un joueur sort, un joueur entre) à un moment donné.
- **Pass** : une passe tentée.
- **PassSucceeded** : une passe réussie.
- **Possession** : ratio ou pourcentage de contrôle du ballon
- **PlayTime** : temps de jeu effectif d’un joueur (dérivé des entrées/sorties et du début/fin de match).
- **GoalConceded** : but encaissé par une équipe (dérivé des buts de l’adversaire).
- **CleanSheet** : une équipe ou un gardien n’a encaissé aucun but sur un match terminé (selon règles).
- **Award** : une récompense attribuée suite au match.
- **BestScorer** : joueur ayant inscrit le plus grand nombre de buts.
- **BestGoalkeeper** : joueur ayant la meilleure performance (le plus grands nombres d' arrêts, buts encaissés, etc.).
- **MVP** : Most Valuable Player, meilleur joueur du match.
- **PlayerRate** : note numérique évaluant la performance globale d’un joueur (saisie ou calculée).
- **MatchStatus** : état du match : `Scheduled`, `InProgress`, `Finished`, `Forfeited`, `Cancelled`.
- **Forfeit (Forfait)** : match perdu/gagné administrativement suite à abandon/absence (score administratif possible).

**Invariants métier**

- Un joueur appartient à une seule équipe dans un match.
- Un évènement référence une équipe qui doit être l’une des deux équipes du match.
- Un évènement (buteur, passeur, sanction, substitution…) ne peut référencer que des joueurs présents sur la feuille de match.
- Aucun évènement de jeu ne peut être ajouté si le match est terminé (`Finished`) ou annulé (`Cancelled`) (hors corrections administratives si autorisées).
- Le score final est calculé à partir des buts (`Goal`) valides (ou remplacé par un score administratif en cas de forfait).
- Les compteurs dérivés doivent rester cohérents :
  - tirs cadrés ≤ tirs,
  - passes réussies ≤ passes.
- Après un carton rouge (`RedCard`), un joueur ne peut plus participer aux actions de jeu (pas d’évènements de jeu postérieurs) et ne peut pas revenir via substitution.
- Le temps de jeu (`PlayTime`) dérivé est toujours ≥ 0.
- `BestScorer` peut être vide (ex : 0–0, match annulé, forfait sans stats).
- Si `BestScorer` est défini, alors le joueur a strictement plus de 0 but et fait partie des joueurs au maximum de buts.
- `CleanSheet` ne peut être vrai que si le match est considéré terminé (ou forfait selon règle) et si les buts encaissés sont égaux à 0.
- Si le match est `Forfeited`, l’équipe forfaitaire et la règle de score administratif (ou le score administratif) doivent être connus, et la politique sur les statistiques (ignorées / conservées / non officielles) doit être explicite.
