# PlayerCreate

**Resume metier**

Cet évènement est reçu lorsqu'un joueur est cree dans le domaine manage-player.

Il permet a manage-team de synchroniser sa projection locale des joueurs.

**Déclencheur**

- Une commande ou une action metier de creation de joueur est validee dans manage-player.

**Payload JSON**

- id - *VO Id*
- displayName - *VO Name / Label metier*

**Invariants**

- id doit être non vide
- id doit être un UUID valide
- displayName doit être non vide

**Format JSON attendu**

```json
{
  "id": "9d8a5fbb-8c71-4c0e-a6f0-5f9b6a4b5d12",
  "displayName": "Player Test"
}
```

- **Schema** : `tests/schemas/PlayerCreate.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerCreate.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerCreate.invalid.json`

**Producteur**

- Service métier manage-player

**Consommateur**

- Service métier manage-team

**Tests minimaux attendus**

- verifier qu'un payload avec id et displayName valides est accepté
- verifier qu'un payload avec un id invalide est rejeté
- verifier qu'un payload avec un displayName vide est rejeté

**Remarques**

Cet évènement entrant représente le fait metier "un joueur a ete cree".
Il ne contient que les informations utiles a la projection de manage-team.

