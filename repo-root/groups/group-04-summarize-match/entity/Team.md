# Entity — Team

**Resume metier**

`Team` represente une equipe dans la composition initiale d'un match.

Dans `summarize-match`, l'equipe est une reference de domaine issue du contexte amont. Le service ne gere pas la fiche equipe complete, seulement son identite et sa composition de depart.

**Attributs**

- **team_id** — *TeamId* — identifiant stable de l'equipe.
- **starting_players** — *Vec<Player>* — joueurs titulaires au demarrage.

**Identite**

L'identite metier est portee par `TeamId`.

`MatchAggregate` conserve `home_team_id` et `away_team_id` apres `MATCH_STARTED` pour valider les buts, forfaits, cartons et remplacements.

**Invariants**

- `team_id` doit etre un UUID valide via le value object `TeamId`.
- Dans `MATCH_STARTED`, chaque equipe doit avoir exactement un gardien.
- Un but ne peut etre attribue qu'a l'equipe domicile ou a l'equipe exterieure du match.

**Cycle de vie dans ce contexte**

- Recu dans `MATCH_STARTED.home_team`.
- Recu dans `MATCH_STARTED.away_team`.
- Transforme ensuite en references `home_team_id` et `away_team_id` dans l'agregat.
- N'est pas persiste comme table dediee.

**Place dans l'architecture hexagonale**

- L'adapter inbound valide le JSON puis mappe vers `Team`.
- L'agregat applique les regles de composition et d'appartenance au match.
- La persistence reste event-sourced via `match_events`.

**References de code**

- Definition: `summarize-match/src/domain/events.rs`
- Usage dans l'agregat: `summarize-match/src/domain/aggregate.rs`
- Value object: `value-object/TeamId.md`
