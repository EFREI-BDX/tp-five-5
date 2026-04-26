# PlayerChangeName

**Resume metier**

Cet evenement est recu lorsqu'un joueur change de nom (displayName) dans le domaine manage-player.

Il permet a manage-team de garder une vue coherente des informations joueur.

**Declencheur**

- Une commande ou une action metier de modification du nom joueur est validee dans manage-player.

**Payload JSON**

- id - *VO Id*
- newDisplayName - *VO Name / Label metier*

**Invariants**

- id doit être non vide
- id doit être un UUID valide
- newDisplayName doit être non vide

**Format JSON attendu**

```json
{
  "id": "9d8a5fbb-8c71-4c0e-a6f0-5f9b6a4b5d12",
  "newDisplayName": "Player Test Updated"
}
```

- **Schema** : `tests/schemas/PlayerChangeName.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerChangeName.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerChangeName.invalid.json`

**Producteur**

- Service metier manage-player

**Consommateur**

- Service metier manage-team

**Tests minimaux attendus**

- verifier qu'un payload avec id et newDisplayName valides est accepte
- verifier qu'un payload avec un id invalide est rejete
- verifier qu'un payload avec un newDisplayName vide est rejete

**Remarques**

Cet evenement entrant represente le fait metier "un joueur a ete renomme".
Il ne contient que les informations utiles a la projection de manage-team.

