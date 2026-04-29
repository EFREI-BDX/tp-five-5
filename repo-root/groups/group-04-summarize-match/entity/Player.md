# Entity — Player

**Resume metier**

`Player` represente un joueur present dans une composition de match.

Dans `summarize-match`, le joueur n'est pas gere comme un agregat autonome : le service utilise surtout son identifiant pour valider et agreger les events de match.

**Attributs**

- **player_id** — *PlayerId* — identifiant stable du joueur.
- **is_goalkeeper** — *bool* — indique si le joueur est gardien dans la composition initiale.

**Identite**

L'identite metier est portee par `PlayerId`.

Deux occurrences de `Player` avec le meme `PlayerId` representent le meme joueur du point de vue de ce bounded context, meme si `summarize-match` ne possede pas la fiche joueur complete.

**Invariants**

- `player_id` doit etre un UUID valide via le value object `PlayerId`.
- Dans `MATCH_STARTED`, chaque equipe doit avoir exactement un joueur avec `is_goalkeeper = true`.
- Les regles sur les joueurs expulses sont portees par `MatchAggregate`, via `expelled_players`.

**Cycle de vie dans ce contexte**

- Recu dans `MATCH_STARTED.home_team.starting_players`.
- Recu dans `MATCH_STARTED.away_team.starting_players`.
- Reference ensuite par les events via `PlayerId`.
- N'est pas persiste comme table dediee.

**Place dans l'architecture hexagonale**

- Les DTO inbound des events sont traduits vers `Player` par les mappers.
- L'agregat utilise `Player` uniquement au demarrage du match pour verifier la composition.
- Les repositories persistent les events bruts, pas une table `players`.

**References de code**

- Definition: `summarize-match/src/domain/events.rs`
- Validation gardien: `summarize-match/src/domain/aggregate.rs`
- Value object: `value-object/PlayerId.md`
