# Event

**Résumé métier**

Représentation d'un type d'événement pouvant être enregistré pendant un match de five. Un Event sert de dictionnaire des actions (ex. `GOAL`, `FOUL`, `SUBEV`), ainsi que le nombre de joueurs impliqués dans cette action. Il sert de référentiel pour valider les MatchEvent.

**Attributs persistés en base / JSON**

- **eventId** - identifiant unique du type d'événement
- **name** - nom ou libellé de l'événement
- **nbPlayers** - nombre de joueurs impliqués dans l'événement

**Attributs domaine**

- **eventId** - identifiant unique du type d'événement, représenté par un `EventId`
- **name** - nom ou libellé de l'événement, représenté par un `EventName`
- **nbPlayers** - cardinalité des joueurs impliqués, représentée par un `PlayerCount`

**Invariants**

- **eventId** doit être un UUID valide et non vide
- **name** doit être une chaîne non vide
- **nbPlayers** doit être un entier compris entre **0** et **2** inclus
- `nbPlayers = 0` signifie aucun joueur requis
- `nbPlayers = 1` signifie un seul joueur requis
- `nbPlayers = 2` signifie deux joueurs requis

**Value Objects utilisés**

- `EventId`
- `EventName`
- `PlayerCount`

**Format JSON attendu**

- **Schéma** : `tests/schemas/event.schema.json`
- **Fixture valide** : `tests/fixtures/event.valid.json`
- **Fixture invalide** : `tests/fixtures/event.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un eventId UUID valide, un name non vide et un nbPlayers valide ne lève pas d'exception.
- **createInvalidIdEventThrows** - eventId non UUID lève une exception métier.
- **createInvalidEmptyNameThrows** - name vide lève une exception métier.
- **createInvalidNbPlayersThrows** - nbPlayers < 0 ou > 2 lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.