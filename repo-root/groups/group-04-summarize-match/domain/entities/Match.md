# Match

**Résumé métier**

Une rencontre de Five (5 vs 5) entre exactement deux équipes. Le `Match` porte l’identité du match, les équipes participantes, l’état du match et la timeline des évènements qui permettront de générer un `Summary` (score, stats, awards), y compris en cas de forfait.

**Attributs**

- **id** — *VO MatchId*
- **homeTeamId** — *VO TeamId* — identifiant de l’équipe domicile
- **awayTeamId** — *VO TeamId* — identifiant de l’équipe extérieure
- **status** — *MatchStatus* — `Scheduled` | `InProgress` | `Finished` | `Forfeited` | `Cancelled`
- **players** — *Player[]* — feuille de match (joueurs participants, rattachés à une équipe)
- **events** — *Event[]* — timeline des évènements du match (ordre chronologique)
- **score** — *VO Score* — score final (peut être calculé à partir des évènements ou administratif en cas de forfait)
- **forfeit** — *Forfeit | null* — informations de forfait (si `status = Forfeited`)

> Remarque : selon l’architecture, `score` peut être stocké dans `Summary` uniquement (dérivé), mais il reste un concept métier clef du match.

**Invariants**

- `id.value` doit être un UUID valide et non vide.
- Un match doit contenir exactement deux équipes :
  - `homeTeamId.value` et `awayTeamId.value` doivent être non vides,
  - `homeTeamId` et `awayTeamId` doivent être différents.
- Un joueur (`Player`) doit appartenir à une seule équipe dans le match (un joueur ne peut pas être à la fois dans l’équipe home et away).
- Un évènement (`Event`) doit référencer uniquement :
  - une `teamId` qui est soit `homeTeamId` soit `awayTeamId`,
  - des `playerId` appartenant à la feuille de match (sauf cas “joueur inconnu” explicitement modélisé).
- Aucun évènement de jeu ne peut être ajouté si `status` vaut `Finished` ou `Cancelled` (hors corrections administratives si elles existent).
- La timeline `events` doit être ordonnée par `EventTimestamp` (si deux events ont le même timestamp, un mécanisme de tie-breaker déterministe doit exister).
- Le score final doit être cohérent :
  - si `status = Finished` alors `score` doit correspondre au nombre de `Goal` valides,
  - si `status = Forfeited` alors `score` peut être un score administratif défini par la règle de forfait.
- Si `status = Forfeited`, alors `forfeit` doit être présent.
- Après un `RedCard`, un joueur ne peut plus participer aux actions de jeu (pas d’évènements de jeu postérieurs) et ne peut pas revenir via substitution.

**Format JSON attendu**

- **Schéma** : `tests/schemas/match.schema.json`
- **Fixture valide** : `tests/fixtures/match.valid.json`
- **Fixture invalide** : `tests/fixtures/match.invalid.json`

**Tests minimaux attendus**

- Créer un match avec 2 équipes distinctes et une feuille de match cohérente → OK.
- Rejeter un match où `homeTeamId == awayTeamId`.
- Rejeter un match avec un `id` invalide (non UUID / vide).
- Rejeter un match contenant un évènement qui référence une équipe inconnue (ni home ni away).
- Rejeter un match contenant un évènement qui référence un joueur absent de la feuille de match.
- Rejeter l’ajout d’un évènement lorsque `status = Finished` ou `Cancelled`.
- Forfait :
  - accepter `status = Forfeited` avec un `forfeit` présent,
  - rejeter `status = Forfeited` sans `forfeit`.

**Génération des fixtures**

- `match.valid.json` :
  - générer `id`, `homeTeamId`, `awayTeamId` comme UUID valides,
  - inclure une feuille de match minimale (ex : 5 joueurs par équipe),
  - inclure 0..n évènements cohérents (ex : un `Goal` valide).
- `match.invalid.json` :
  - cas 1 : `homeTeamId == awayTeamId`,
  - cas 2 : `id = "not-a-uuid"`,
  - cas 3 : un `Event.teamId` qui ne correspond à aucune des équipes du match,
  - cas 4 : `status = Forfeited` sans `forfeit`.