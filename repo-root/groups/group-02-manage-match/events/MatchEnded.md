# MatchEnded

**Résumé métier**

Cet évènement est émis lorsqu'un match se termine.

Il permet d'indiquer qu'un match identifié par son id est terminé.

**Déclencheur**

- Une commande ou une action métier de clôture de match est validée.
- La clôture est acceptée uniquement si le match est bien en cours et que les règles métier autorisent sa fin.

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

- **Schéma** : `tests/schemas/MatchEnded.schema.json`
- **Fixture valide** : `tests/fixtures/MatchEnded.valid.json`
- **Fixture invalide** : `tests/fixtures/MatchEnded.invalid.json`

**Producteur**

- Aggregate / service métier de gestion de match

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- calcul du classement des équipes
- historique des résultats

**Tests minimaux attendus**

- vérifier qu'un payload avec matchId valide est accepté
- vérifier qu'un payload avec un matchId invalide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "le match est terminé".
Il ne contient volontairement que les données utiles à ce fait.
Les scores sont des entiers représentant le nombre de buts marqués par chaque équipe.
