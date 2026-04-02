# PlayerDelete

**Resume metier**

Cet évènement est reçu lorsqu'un joueur est supprimé dans le domaine manage-player.

Il permet a manage-team de retirer ce joueur de sa projection locale.

**Déclencheur**

- Une commande ou une action metier de suppression de joueur est validée dans manage-player.

**Payload JSON**

- id - *VO Id*

**Invariants**

- id doit être non vide
- id doit être un UUID valide

**Format JSON attendu**

```json
{
  "id": "9d8a5fbb-8c71-4c0e-a6f0-5f9b6a4b5d12"
}
```

- **Schema** : `tests/schemas/PlayerDelete.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerDelete.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerDelete.invalid.json`

**Producteur**

- Service metier manage-player

**Consommateur**

- Service metier manage-team

**Tests minimaux attendus**

- verifier qu'un payload avec id valide est accepte
- verifier qu'un payload avec un id invalide est rejete
- verifier qu'un payload avec un id vide est rejete

**Remarques**

Cet évènement entrant représente le fait metier "un joueur a ete supprime".
Il ne contient que les informations utiles a la projection de manage-team.

