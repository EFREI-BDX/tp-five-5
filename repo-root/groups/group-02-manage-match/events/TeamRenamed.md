# TeamRenamed

**Résumé métier**

Cet évènement est émis lorsqu'une équipe change de nom.

Il permet d'indiquer qu'une équipe identifiée par son id a été renommée avec une nouvelle valeur de label.

**Déclencheur**

- L'évènement `TeamRenamed` est produit par le service **Manage Team** (groupe-06).
- Le service Manage Match consomme cet évènement pour mettre à jour la référence du nom de l'équipe dans les matchs en cours et dans l'historique des matchs.

**Payload JSON**

- id — *VO Id*
- newLabel — *VO Label*

**Invariants**

- id doit être non vide
- id doit être un UUID valide
- newLabel doit être non vide
- newLabel doit respecter les règles métier de nommage d'une équipe

**Format JSON attendu**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "newLabel": "Paris Five Stars"
}
```

- **Schéma** : `tests/schemas/TeamRenamed.schema.json`
- **Fixture valide** : `tests/fixtures/TeamRenamed.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamRenamed.invalid.json`

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
Le service Manage Match le consomme afin de maintenir la cohérence des noms d'équipes affichés dans les résultats et matchs en cours.
