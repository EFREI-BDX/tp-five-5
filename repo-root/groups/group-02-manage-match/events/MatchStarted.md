# MatchStarted

**Résumé métier**

Cet évènement est émis lorsqu'un match commence.

Il permet d'indiquer qu'un match identifié par son id a débuté entre deux équipes, à un instant précis.

**Déclencheur**

- Une commande ou une action métier de démarrage de match est validée.
- Le démarrage est accepté uniquement si les règles métier autorisent le début du match (les deux équipes existent et sont disponibles).

**Payload JSON**

- matchId — *VO Id*


**Invariants**

- matchId doit être non vide
- matchId doit être un UUID valide

**Format JSON attendu**

```json
{
  "matchId": "550e8400-e29b-41d4-a716-446655440000"
}
```

- **Schéma** : `tests/schemas/MatchStarted.schema.json`
- **Fixture valide** : `tests/fixtures/MatchStarted.valid.json`
- **Fixture invalide** : `tests/fixtures/MatchStarted.invalid.json`

**Producteur**

- Aggregate / service métier de gestion de match

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- affichage en temps réel du match en cours

**Tests minimaux attendus**

- vérifier qu'un payload avec matchId valide est accepté
- vérifier qu'un payload avec un matchId invalide est rejeté
- vérifier qu'un payload avec un champ manquant est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "le match a commencé".
Il ne contient volontairement que les données utiles à ce fait.
Les scores ne sont pas inclus à ce stade car ils sont nuls au début du match.
