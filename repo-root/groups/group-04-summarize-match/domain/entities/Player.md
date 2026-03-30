# Player

**Résumé métier**

Un joueur participant à un match de Five. Il appartient à une seule équipe dans le contexte du match et peut être référencé par les évènements (but, passe, faute, carton, substitution…) ainsi que par les statistiques et awards du `Summary`.

**Attributs**

- **id** — *VO PlayerId* — identifiant unique du joueur (UUID)
- **displayName** — *string* — nom d’affichage du joueur
- **teamId** — *VO TeamId* — identifiant de l’équipe du joueur dans ce match
- **shirtNumber** — *integer (optionnel)* — numéro de maillot
- **isTeamLeader** — *boolean (optionnel)* — indique si le joueur est le leader de l’équipe (dans ce match)

**Invariants**

- `id` doit être non vide et doit correspondre à un `PlayerId` valide (UUID).
- `displayName` doit être non vide (après trim).
- `teamId` doit être non vide et doit correspondre à un `TeamId` valide (UUID).
- Un joueur appartient à une seule équipe dans un même match (pas de changement d’équipe intra-match).
- Un joueur référencé par un `Event` doit exister dans la feuille de match (invariant généralement garanti par l’agrégat `Match`).

**Format JSON attendu**

- **Schéma** : `tests/schemas/player.schema.json`
- **Fixture valide** : `tests/fixtures/player.valid.json`
- **Fixture invalide** : `tests/fixtures/player.invalid.json`

**Tests minimaux attendus**

- Accepter un joueur avec :
  - `id` UUID valide,
  - `displayName` non vide,
  - `teamId` UUID valide.
- Rejeter :
  - `id` manquant / vide / non UUID,
  - `displayName` manquant / vide / uniquement espaces,
  - `teamId` manquant / vide / non UUID.
- (Si testé au niveau agrégat `Match`) rejeter un `Event` qui référence un `PlayerId` non présent dans la feuille de match.

**Génération des fixtures**

- `player.valid.json` : générer `id` et `teamId` avec des UUID valides, et un `displayName` non vide.
- `player.invalid.json` : produire au moins un cas invalide (ex : `id = "not-a-uuid"` ou `displayName = ""` ou `teamId` manquant).