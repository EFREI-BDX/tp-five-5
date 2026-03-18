# Domain Summary — Match Management

**Objectif métier**  
Gérer la création et l'état des matchs sportifs afin de permettre la planification, l'organisation et le suivi des rencontres entre équipes sur des terrains dédiés. Ce bounded context interagit avec les autres contextes pour la gestion des équipes, des terrains.

**Ubiquitous language**

- **Match** — entité représentant une rencontre sportive entre deux équipes (id, period, matchState).
- **Team** — équipe participant à un match (id, tag, label, teamState).
- **Field** — lieu où se déroule un match (id, label).
- **Period** — période ou durée du match.
- **Statut** — état actuel du match (planifié, en cours, terminé, annulé).
- **Tag** — identifiant court d'une équipe.
- **MatchState** — état actuel d'un match matchState (pas démarré, en cours, terminé, annulé).
- **TeamState** — état actuel d'une équipe (incomplète, complète et dissoute).

**Principales invariants métier**

- **Match.id** doit être un identifiant unique.
- **Match.period** doit être une valeur positive représentant la durée ou période du match.
- **Match.matchState** doit appartenir à l'énumération autorisée (`planned`, `in_progress`, `completed`, `cancelled`).
- **Team.id** doit être un identifiant unique.
- **Team.tag** ne peut pas être vide et doit être unique dans le système et contenir 3 caractères ou moins.
- **Team.label** ne peut pas être vide.
- **Team.teamState** doit appartenir à l'énumération autorisée (`active`, `inactive`, `suspended`).
- **Field.id** doit être un identifiant unique.
- **Field.label** ne peut pas être vide.
- Un match doit avoir exactement deux équipes participantes.
- Un terrain ne peut accueillir qu'un seul match à la fois pour une période donnée.

**Principaux events produits**

- **MatchCreated** — payload minimal : `id`, `homeTeamId`, `awayTeamId`, `terrainId`, `period`, `timestamp`.
- **MatchStarted** — payload minimal : `matchId`, `startTime`, `timestamp`.
- **MatchEnded** — payload minimal : `matchId`, `endTime`, `finalScore`, `timestamp`.
- **MatchCancelled** — payload minimal : `matchId`, `reason`, `timestamp`.
- **FieldAssigned** — payload minimal : `matchId`, `terrainId`, `timestamp`.
- **TeamRegistered** — payload minimal : `teamId`, `tag`, `label`, `state`, `timestamp`.