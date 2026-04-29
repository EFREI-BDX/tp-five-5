# Read Models

Contrats de lecture exposes par le bounded context `resume-match`.

Ces modeles ne sont pas des events de transport. Ils sont reconstruits par replay des events stockes via `MatchRepository`, puis exposes par les routes query REST.

- [MatchSummary](MatchSummary.md) — resume courant du match.
- [PlayerMatchStats](PlayerMatchStats.md) — statistiques calculees par joueur.

Les contrats JSON Schema sont versionnes dans `../tests/schemas/` et valides par les tests d'integration HTTP.
