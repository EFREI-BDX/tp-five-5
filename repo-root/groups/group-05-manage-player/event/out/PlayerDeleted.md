# PlayerDeleted

**Résumé métier**

Cet évènement est émis lorsqu'un joueur est supprimé.

Il permet d'indiquer qu'un joueur identifié par son id a été supprimé du système.

**Déclencheur**

- Une commande ou une action métier de suppression d'un joueur est validée.
- La suppression est acceptée uniquement si les invariants métier du joueur sont respectés.

**Payload JSON**

- playerId — *VO Id*

**Invariants**

- playerId doit être non vide
- playerId doit être un UUID valide
- playerId doit correspondre à un joueur existant

**Format JSON attendu**

```json
{
  "playerId": "550e8400-e29b-41d4-a716-446655440000"
}
```

- **Schéma** : `tests/schemas/PlayerDeleted.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerDeleted.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerDeleted.invalid.json`

**Producteur**

- Aggregate / service métier de gestion de joueur

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- synchronisation avec un autre système
- archivage

**Tests minimaux attendus**

- vérifier qu'un payload avec playerId valide est accepté
- vérifier qu'un payload avec un playerId invalide est rejeté
- vérifier qu'un payload avec un identifiant vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "un joueur a été supprimé".
Il ne contient volontairement que les données utiles à ce fait.
