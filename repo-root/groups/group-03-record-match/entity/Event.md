# Event

**Résumé métier**

Représentation d'un type d'événement pouvant être enregistré pendant un match de five. Un Event décrit la nature de l'action observée, ainsi que le nombre de joueurs impliqués dans cette action. Il sert de référentiel pour valider les MatchEvent.

**Attributs persistés en base / JSON**

- **IdEvent** - identifiant unique du type d'événement
- **Name** - nom ou libellé de l'événement
- **NbPlayer** - nombre de joueurs impliqués dans l'événement

**Attributs domaine**

- **idEvent** - identifiant unique du type d'événement, représenté par un `EventId`
- **name** - nom ou libellé de l'événement, représenté par un `EventName`
- **nbPlayer** - nombre de joueurs impliqués, représenté par un `PlayerCount`

**Invariants**

- **idEvent** doit être un UUID valide et non vide
- **name** doit être une chaîne non vide
- **nbPlayer** doit être un entier positif ou nul
- **nbPlayer** doit correspondre au nombre de joueurs réellement nécessaires pour enregistrer cet événement
- **nbPlayer** vaut généralement 0, 1 ou 2 selon la nature de l'événement

**Value Objects utilisés**

- `EventId`
- `EventName`
- `PlayerCount`

**Format JSON attendu**

- **Schéma** : `tests/schemas/event.schema.json`
- **Fixture valide** : `tests/fixtures/event.valid.json`
- **Fixture invalide** : `tests/fixtures/event.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un idEvent UUID valide, un name non vide et un nbPlayer valide ne lève pas d'exception.
- **createInvalidIdEventThrows** - idEvent non UUID lève une exception métier.
- **createInvalidEmptyNameThrows** - name vide lève une exception métier.
- **createInvalidNbPlayerThrows** - nbPlayer négatif ou incohérent lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.