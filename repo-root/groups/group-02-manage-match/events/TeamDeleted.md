# TeamDeleted

**Résumé métier**

Cet évènement est émis lorsqu'une équipe est supprimée.

Il permet d'indiquer qu'une équipe identifiée par son id n'existe plus dans le système.

**Déclencheur**

- L'évènement `TeamDeleted` est produit par le service **Manage Team** (groupe-06).
- Le service Manage Match consomme cet évènement pour annuler ou marquer comme invalides les matchs planifiés ou en cours impliquant cette équipe.

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

- Aggregate / service métier de gestion d'équipe (**groupe-06-manage-team**)

**Consommateurs possibles**

- **Manage Match (groupe-02)** : annulation ou invalidation des matchs planifiés ou en cours impliquant l'équipe supprimée
- journalisation métier
- projection de lecture
- audit
- synchronisation avec un autre système

**Tests minimaux attendus**

- vérifier que la réception d'un payload avec un id valide est traitée correctement
- vérifier qu'un payload avec un id invalide est rejeté
- vérifier qu'un payload avec un id vide est rejeté

**Remarques**

Cet évènement est produit par le service Manage Team.
Le service Manage Match le consomme afin d'assurer la cohérence des données : tout match impliquant une équipe supprimée doit être annulé ou marqué comme invalide.
