# PlayerData

## Resume metier

Evenement metier emis a la fin d'un match pour decrire la performance individuelle d'un joueur. Il alimente les statistiques, le recapitulatif de match et les classements.

## Declencheur

Evenement publie a la fin du match, pour chaque joueur ayant participe, une fois les statistiques consolidees.

## Contrat de l'evenement

- Type d'evenement : `PlayerData`
- Version du contrat : `v1`
- Convention de nommage : `camelCase`, avec `MVP` conserve en majuscules (contrat source)

## Payload JSON

```json
{
  "palyerId": "550e8400-e29b-41d4-a716-446655440000",
  "goals": 2,
  "assists": 1,
  "saves": 4,
  "result": "Win",
  "bestScorer": true,
  "bestAssistsProvider": false,
  "MVP": true,
  "playTime": 5400
}
```

## Attributs

| Champ | Type | Regle | Description |
|---|---|---|---|
| `palyerId` | string (UUID) | non vide, format UUID | Identifiant unique du joueur |
| `goals` | integer | `>= 0` | Nombre de buts |
| `assists` | integer | `>= 0` | Nombre de passes decisives |
| `saves` | integer | `>= 0` | Nombre d'arrets |
| `result` | string | enum `Win`, `Loose`, `Draw` | Resultat du joueur pour le match |
| `bestScorer` | boolean | obligatoire | Indique si le joueur est meilleur buteur du match |
| `bestAssistsProvider` | boolean | obligatoire | Indique si le joueur est meilleur passeur du match |
| `MVP` | boolean | obligatoire | Indique si le joueur est MVP du match |
| `playTime` | integer | `>= 0` | Temps de jeu en secondes |

## Invariants metier

- `palyerId` doit etre un UUID valide.
- `goals >= 0`.
- `assists >= 0`.
- `saves >= 0`.
- `playTime >= 0`.
- `result` appartient strictement a l'ensemble `{Win, Loose, Draw}`.

## Format JSON attendu

- Schema : `tests/schemas/player-data.schema.json`
- Fixture valide : `tests/fixtures/player-data.valid.json`
- Fixture invalide : `tests/fixtures/player-data.invalid.json`
- Objet JSON plat contenant exactement les cles : `palyerId`, `goals`, `assists`, `saves`, `result`, `bestScorer`, `bestAssistsProvider`, `MVP`, `playTime`.
- Aucun champ metier obligatoire ne doit etre `null`.

