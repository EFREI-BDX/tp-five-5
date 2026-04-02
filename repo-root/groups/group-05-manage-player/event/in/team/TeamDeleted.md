# TeamDeleted

**Résumé métier**

Cet évènement est émis lorsqu'une équipe est supprimée.

Il permet d'indiquer qu'une équipe identifiée par son id a été supprimée du système.

**Déclencheur**

- Une commande ou une action métier de suppression d'une équipe est validée.
- La suppression est acceptée uniquement si les invariants métier de l'équipe sont respectés.

**Payload JSON**

- teamId — *VO Id*

**Invariants**

- teamId doit être non vide
- teamId doit être un UUID valide
- teamId doit correspondre à une équipe existante

**Format JSON attendu**

```json
{
  "teamId": "550e8400-e29b-41d4-a716-446655440000"
}
```

- **Schéma** : `tests/schemas/TeamDeleted.schema.json`
- **Fixture valide** : `tests/fixtures/TeamDeleted.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamDeleted.invalid.json`

**Producteur**

- Aggregate / service métier de gestion d'équipe

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- synchronisation avec un autre système
- archivage

**Tests minimaux attendus**

- vérifier qu'un payload avec teamId valide est accepté
- vérifier qu'un payload avec un teamId invalide est rejeté
- vérifier qu'un payload avec un identifiant vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "une équipe a été supprimée".
Il ne contient volontairement que les données utiles à ce fait.
