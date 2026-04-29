# Entities — index

Fiches par entite ou objet avec identite pour le domaine `summarize-match`.

- [Player](Player.md) — joueur identifie par `PlayerId` dans une composition ou un event.
- [Team](Team.md) — equipe identifiee par `TeamId` dans `MATCH_STARTED`.
- [GoalEntry](GoalEntry.md) — entree de but du read model `MatchSummary`.
- [CardEntry](CardEntry.md) — entree de carton du read model `MatchSummary`.
- [SubstitutionEntry](SubstitutionEntry.md) — entree de remplacement du read model `MatchSummary`.

## Note DDD

Dans le code actuel, `Player` et `Team` sont des structures de domaine transportees par les events, mais leur cycle de vie complet n'est pas gere par `summarize-match`. Le service manipule surtout leurs identifiants.

`GoalEntry`, `CardEntry` et `SubstitutionEntry` sont des elements de read model derives de l'agregat. Ils sont documentes ici car ils portent une identite d'event et representent des faits metier visibles, mais ils ne sont pas des entites persistantes autonomes.
