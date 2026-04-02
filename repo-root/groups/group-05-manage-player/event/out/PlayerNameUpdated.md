# PlayerNameUpdated

**Résumé métier**

Cet évènement est émis lorsqu'un joueur modifie son prénom ou son nom.

Il permet d'indiquer qu'un joueur identifié par son id a changé son prénom et/ou son nom.

**Déclencheur**

- Une commande ou une action métier de modification du nom d'un joueur est validée.
- La modification est acceptée uniquement si les invariants métier du joueur sont respectés.

**Payload JSON**

- playerId — *VO Id*
- firstName — *VO FirstName*
- lastName — *VO LastName*

**Invariants**

- playerId doit être non vide
- playerId doit être un UUID valide
- firstName doit être non vide
- firstName doit respecter le format défini (entre 2 et 50 caractères)
- lastName doit être non vide
- lastName doit respecter le format défini (entre 2 et 50 caractères)

**Format JSON attendu**

```json
{
  "playerId": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "Jean",
  "lastName": "Dupont"
}
```

- **Schéma** : `tests/schemas/PlayerNameUpdated.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerNameUpdated.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerNameUpdated.invalid.json`

**Producteur**

- Aggregate / service métier de gestion de joueur

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- synchronisation avec un autre système

**Tests minimaux attendus**

- vérifier qu'un payload avec playerId, firstName et lastName valides est accepté
- vérifier qu'un payload avec un playerId invalide est rejeté
- vérifier qu'un payload avec un firstName vide est rejeté
- vérifier qu'un payload avec un lastName vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "un joueur a changé de nom".
Il ne contient volontairement que les données utiles à ce fait.
