# Team

**Résumé métier**

Une `Team` représente une équipe participant à un match de Five.  
Elle sert à identifier l’équipe (id + nom) et à porter la feuille de match (joueurs, leader) dans le contexte du match.

**Attributs**

- **id** — *VO TeamId* — identifiant unique de l’équipe
- **name** — *string* — nom d’affichage de l’équipe
- **playerIds** — *array<VO PlayerId>* — liste des joueurs inscrits sur la feuille de match (participants)
- **teamLeaderId** — *VO PlayerId (optionnel)* — joueur désigné leader de l’équipe
- **side** — *string (enum)* — côté dans le match : `HOME` ou `AWAY`
- **substituteIds** — *array<VO PlayerId> (optionnel)* — remplaçants autorisés (si la règle locale l’autorise)

**Invariants**

- `id` doit être non vide et doit être un UUID valide (voir VO `TeamId`).
- `name` doit être non vide (après trim).
- `playerIds` ne doit pas contenir de doublons.
- Une équipe doit avoir au minimum **5 joueurs** déclarés sur la feuille de match (si vous autorisez les remplaçants : `playerIds.length >= 5`).
- Si `teamLeaderId` est défini, alors `teamLeaderId` doit appartenir à la feuille de match de l’équipe (`playerIds` ou `substituteIds` si présent).
- `side` doit être soit `HOME`, soit `AWAY`.

**Format JSON attendu**

- **Schéma** : `tests/schemas/team.schema.json`
- **Fixture valide** : `tests/fixtures/team.valid.json`
- **Fixture invalide** : `tests/fixtures/team.invalid.json`

**Tests minimaux attendus**

- Accepter une équipe avec :
  - `id` UUID valide
  - `name` non vide
  - `playerIds` contenant au moins 5 `PlayerId` distincts
  - `side` ∈ {`HOME`, `AWAY`}
- Rejeter :
  - `id` manquant / non UUID
  - `name` vide
  - `playerIds` avec doublons
  - `playerIds` avec moins de 5 joueurs
  - `teamLeaderId` non présent dans la feuille de match
  - `side` différent de `HOME`/`AWAY`

**Génération des fixtures**

- `team.valid.json` : générer un UUID pour `id`, un nom non vide, 5 UUID distincts pour `playerIds`, et `side="HOME"` (ou `"AWAY"`).
- `team.invalid.json` : générer au moins un cas non conforme, par exemple :
  - `id="not-a-uuid"` ou `name=""` ou `playerIds` avec doublons ou seulement 4 joueurs.