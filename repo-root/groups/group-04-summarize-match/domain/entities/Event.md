# Event

**Résumé métier**

Un `Event` est un fait de jeu horodaté survenu pendant un `Match` (ex : but, faute, carton, tir, passe, substitution…).  
La timeline des `Event` est la source permettant de recalculer le `Score`, les statistiques et le `Summary` d’un match.

**Attributs**

- id — *VO EventId (UUID)* — identifiant unique de l’évènement
- matchId — *VO MatchId (UUID)* — identifiant du match auquel l’évènement appartient
- type — *EventType* — type d’évènement (ex : `Goal`, `Foul`, `YellowCard`, `RedCard`, `Shot`, `ShotOnTarget`, `Pass`, `PassSucceeded`, `Save`, `Substitution`, `Possession`)
- timestamp — *VO EventTimestamp* — moment de l’évènement dans le match (ex : `minute`/`second`)
- teamId — *VO TeamId (UUID)* — équipe à laquelle l’évènement est attribué
- actorPlayerId — *VO PlayerId (UUID), optionnel* — joueur auteur de l’action (selon le type)
- payload — *object, optionnel* — données spécifiques au type (ex : `assistPlayerId`, `playerInId`, `playerOutId`, `receiverPlayerId`, etc.)

**Invariants**

- `id` doit être non vide et être un UUID valide.
- `matchId` doit être non vide et être un UUID valide.
- `type` doit être non vide et appartenir à la liste des types autorisés.
- `timestamp.minute` doit être un entier ≥ 0.
- `timestamp.second` doit être un entier compris entre 0 et 59.
- `teamId` doit être non vide et être un UUID valide.
- Tous les `playerId` référencés par un `Event` (actor, scorer, assist, in/out, receiver, …) doivent être des UUID valides.
- Un `Event` ne peut référencer que :
  - une `teamId` qui est l’une des deux équipes du match (invariant porté par `Match`),
  - des `playerId` présents sur la feuille de match (invariant porté par `Match`).

Règles par type (minimum) :

- `Goal` :
  - le buteur doit être présent (ex : `payload.scorerPlayerId` requis si vous l’utilisez),
  - si `assistPlayerId` est présent alors `assistPlayerId` ≠ buteur et appartient à la même équipe.
- `Substitution` :
  - `playerOutId` et `playerInId` sont requis,
  - `playerOutId` ≠ `playerInId`,
  - les deux joueurs appartiennent à la même équipe (`teamId`).
- `PassSucceeded` :
  - `passerPlayerId` et `receiverPlayerId` sont requis,
  - `passerPlayerId` ≠ `receiverPlayerId`.
- `RedCard` :
  - le joueur sanctionné est requis,
  - après un `RedCard`, le joueur ne peut plus participer à des actions de jeu (invariant porté par `Match`).

**Format JSON attendu**

- **Schéma** : `tests/schemas/event.schema.json`
- **Fixture valide** : `tests/fixtures/event.valid.json`
- **Fixture invalide** : `tests/fixtures/event.invalid.json`

**Tests minimaux attendus**

- Accepter un `Event` valide avec :
  - `id` UUID, `matchId` UUID, `teamId` UUID,
  - `type` dans la liste,
  - `timestamp` valide (`minute >= 0`, `0 <= second <= 59`).
- Rejeter :
  - `id` manquant / non UUID,
  - `type` inconnu,
  - `timestamp.second` > 59 ou `minute` < 0,
  - champs obligatoires manquants (`matchId`, `teamId`, etc.).
- Tests par type :
  - `Goal` sans buteur (si buteur requis) → rejet
  - `Substitution` avec `playerInId == playerOutId` → rejet
  - `PassSucceeded` sans `receiverPlayerId` → rejet

**Génération des fixtures**

- `event.valid.json` :
  - générer des UUID valides pour `id`, `matchId`, `teamId` et `actorPlayerId` (si présent),
  - choisir un `type` simple (ex : `Goal`) et un `timestamp` valide (ex : `{ "minute": 3, "second": 12 }`),
  - ajouter un `payload` minimal cohérent (ex : `scorerPlayerId` UUID).
- `event.invalid.json` :
  - utiliser au moins une violation d’invariant (ex : `id = "not-a-uuid"` ou `timestamp.second = 60`).