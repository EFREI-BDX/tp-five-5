# TeamRenamed

**Résumé métier**

Cet évènement est émis lorsqu'une équipe change de nom.

Il permet d'indiquer qu'une équipe identifiée par son id a été modifiée.

**Déclencheur**

- L'évènement `TeamUpdated` est produit par le service **Manage Team** (groupe-06).
- Le service Manage Match consomme cet évènement pour mettre à jour la référence dans les matchs en cours et dans l'historique des matchs.

**Payload JSON**

**Invariants**

**Format JSON attendu**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000"
}
```

- **Schéma** : `tests/schemas/TeamUpdated.schema.json`
- **Fixture valide** : `tests/fixtures/TeamUpdated.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamUpdated.invalid.json`

**Producteur**

- Aggregate / service métier de gestion d'équipe (**groupe-06-manage-team**)

**Consommateurs possibles**

- **Manage Match (groupe-02)** : mise à jour du nom de l'équipe dans les matchs en cours et l'historique
- journalisation métier
- projection de lecture
- audit

**Tests minimaux attendus**

- vérifier que la réception d'un payload avec un id valide et un newLabel valide est traitée correctement
- vérifier qu'un payload avec un id invalide est rejeté
- vérifier qu'un payload avec un newLabel vide est rejeté

**Remarques**

Cet évènement est produit par le service Manage Team.
Le service Manage Match le consomme afin de maintenir la cohérence des équipes affichés dans les résultats et matchs en cours.
