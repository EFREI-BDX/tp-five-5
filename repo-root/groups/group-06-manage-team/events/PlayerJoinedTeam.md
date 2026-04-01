# PlayerJoinedTeam

**Résumé métier**

Cet évènement est émis lorsqu'un joueur rejoint une équipe.

Il permet d'indiquer qu'un joueur identifié par son id a été ajouté à une équipe identifiée par son id.

**Déclencheur**

- Une commande ou une action métier d'ajout d'un joueur dans une équipe est validée.
- L'ajout est accepté uniquement si les invariants métier de l'équipe sont respectés.

**Payload JSON**

- teamId — *VO Id*
- playerId — *VO Id*

**Invariants**

- teamId doit être non vide
- teamId doit être un UUID valide
- playerId doit être non vide
- playerId doit être un UUID valide
- playerId doit correspondre à un joueur existant si cette règle est retenue
- teamId doit correspondre à une équipe existante si cette règle est retenue
- le joueur ne doit pas déjà appartenir à l'équipe si cette règle est retenue

**Format JSON attendu**

```json
{
  "teamId": "550e8400-e29b-41d4-a716-446655440000",
  "playerId": "9d8a5fbb-8c71-4c0e-a6f0-5f9b6a4b5d12"
}
```

- **Schéma** : `tests/schemas/PlayerJoinedTeam.schema.json`
- **Fixture valide** : `tests/fixtures/PlayerJoinedTeam.valid.json`
- **Fixture invalide** : `tests/fixtures/PlayerJoinedTeam.invalid.json`

**Producteur**

- Aggregate / service métier de gestion d'équipe

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- synchronisation avec un autre système

**Tests minimaux attendus**

- vérifier qu'un payload avec teamId et playerId valides est accepté
- vérifier qu'un payload avec un teamId invalide est rejeté
- vérifier qu'un payload avec un playerId invalide est rejeté
- vérifier qu'un payload avec un identifiant vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "un joueur a rejoint une équipe".
Il ne contient volontairement que les données utiles à ce fait.

