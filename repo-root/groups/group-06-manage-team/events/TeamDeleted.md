# TeamDeleted

**Résumé métier**

Cet évènement est émis lorsqu'une équipe est supprimée.

Il permet d'indiquer qu'une équipe identifiée par son id n'existe plus dans le système.

**Déclencheur**

- Une commande ou une action métier de suppression d'équipe est validée.
- La suppression est acceptée uniquement si les règles métier autorisent la suppression de l'équipe.

**Payload JSON**

- id — *VO Id*

**Invariants**

- id doit être non vide
- id doit être un UUID valide

**Format JSON attendu**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000"
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

**Tests minimaux attendus**

- vérifier qu'un payload avec un id valide est accepté
- vérifier qu'un payload avec un id invalide est rejeté
- vérifier qu'un payload avec un id vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "l'équipe a été supprimée".
Il ne contient volontairement que les données utiles à ce fait.

