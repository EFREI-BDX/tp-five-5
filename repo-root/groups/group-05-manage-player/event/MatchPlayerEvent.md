# MatchPlayerEvent

**Résumé métier**

Cet évènement est reçu lorsqu'un match envoie une information concernant un joueur (mise à jour des statistiques).

Il permet de mettre à jour les statistiques de performance d'un joueur identifié par son id à partir des données du match.

**Déclencheur**

- Une source externe (service match) envoie les statistiques mises à jour d'un joueur.
- Les statistiques sont acceptées uniquement si les invariants métier sont respectés.

**Payload JSON**

- playerId — *VO Id*
- matchesPlayed — *Integer* (≥ 0)
- goalsScored — *Integer* (≥ 0)
- assists — *Integer* (≥ 0)
- wins — *Integer* (≥ 0)
- draws — *Integer* (≥ 0)
- mvps — *Integer* (≥ 0)

**Invariants**

- playerId doit être non vide
- playerId doit être un UUID valide
- matchesPlayed doit être ≥ 0
- goalsScored doit être ≥ 0
- assists doit être ≥ 0
- wins doit être ≥ 0
- draws doit être ≥ 0
- mvps doit être ≥ 0
- wins doit être ≤ matchesPlayed
- draws doit être ≤ matchesPlayed
- mvps doit être ≤ matchesPlayed
- playerId doit correspondre à un joueur existant si cette règle est retenue

**Format JSON attendu**

```json
{
  "playerId": "550e8400-e29b-41d4-a716-446655440000",
  "matchesPlayed": 1,
  "goalsScored": 2,
  "assists": 1,
  "wins": 1,
  "draws": 0,
  "mvps": 1
}
```

- **Schéma** : `tests/schemas/MatchPlayerEvent.schema.json`
- **Fixture valide** : `tests/fixtures/MatchPlayerEvent.valid.json`
- **Fixture invalide** : `tests/fixtures/MatchPlayerEvent.invalid.json`

**Producteur**

- Service externe de gestion des matchs

**Consommateurs possibles**

- journalisation métier
- mise à jour des statistiques du joueur
- projection de lecture
- audit
- synchronisation avec un autre système

**Tests minimaux attendus**

- vérifier qu'un payload avec toutes les statistiques valides et cohérentes est accepté
- vérifier qu'un payload avec un playerId invalide est rejeté
- vérifier qu'un payload avec une valeur négative de statistiques est rejeté
- vérifier qu'un payload avec wins > matchesPlayed est rejeté
- vérifier qu'un payload avec draws > matchesPlayed est rejeté
- vérifier qu'un payload avec mvps > matchesPlayed est rejeté

**Remarques**

Cet évènement ne représente que les statistiques d'un joueur mises à jour après un match.
Il ne contient volontairement que les données utiles à ce fait.
Les statistiques reçues sont cumulatives (elles représentent le total) et doivent être traitées par les consommateurs pour mettre à jour les données du joueur.
