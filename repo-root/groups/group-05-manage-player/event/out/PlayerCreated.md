# PlayerCreated

**Résumé métier**

Cet évènement est émis lorsqu'un joueur est créé.

Il permet d'indiquer qu'un joueur identifié par son id a été ajouté au système avec ses informations de base.

**Déclencheur**

- Une commande ou une action métier de création de joueur est validée.
- La création est acceptée uniquement si les invariants métier du joueur sont respectés.

**Payload JSON**

- playerId — *VO Id*
- firstName — *VO FirstName*
- lastName — *VO LastName*
- email — *VO Email*
- status — *VO Status*

**Invariants**

- playerId doit être non vide
- playerId doit être un UUID valide
- firstName doit être non vide
- firstName doit respecter le format défini (entre 2 et 50 caractères)
- lastName doit être non vide
- lastName doit respecter le format défini (entre 2 et 50 caractères)
- email doit être non vide
- email doit respecter le format d'email valide
- status doit être non vide
- status doit être un des statuts valides (ACTIF, INACTIF, SUPPRIME)

**Format JSON attendu**

```json
{
  "playerId": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "Jean",
  "lastName": "Dupont",
  "email": "jean.dupont@example.com",
  "status": "ACTIF"
}
```

- **Schéma** : `tests/schemas/PlayerCreated.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerCreated.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerCreated.invalid.json`

**Producteur**

- Aggregate / service métier de gestion de joueur

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- synchronisation avec un autre système

**Tests minimaux attendus**

- vérifier qu'un payload avec toutes les données valides est accepté
- vérifier qu'un payload avec un playerId invalide est rejeté
- vérifier qu'un payload avec un email invalide est rejeté
- vérifier qu'un payload avec un champ vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "un joueur a été créé".
Il ne contient volontairement que les données utiles à ce fait.
