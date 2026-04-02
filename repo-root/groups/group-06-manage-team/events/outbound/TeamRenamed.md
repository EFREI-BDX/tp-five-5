# TeamRenamed

**Résumé métier**

Cet évènement est émis lorsqu'une équipe change de nom.

Il permet d'indiquer qu'une équipe identifiée par son id a été renommée avec une nouvelle valeur de label.

**Déclencheur**

- Une commande ou une action métier de renommage d'équipe est validée.
- Le changement est accepté uniquement si le nouveau label respecte les invariants métier.

**Payload JSON**

- id — *VO Id*
- newLabel — *VO Label*

**Invariants**

- id doit être non vide
- id doit être un UUID valide
- newLabel doit être non vide
- newLabel doit respecter les règles métier de nommage d'une équipe
- newLabel doit être différent de l'ancien label si cette règle est retenue

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

- Aggregate / service métier de gestion d'équipe

**Consommateurs**

- 02-manage-match
- 05-manage-player

**Tests minimaux attendus**

- vérifier qu'un payload avec un id valide et un newLabel valide est accepté
- vérifier qu'un payload avec un id invalide est rejeté
- vérifier qu'un payload avec un newLabel vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "l'équipe a été renommée".
Il ne contient volontairement que les données utiles à ce fait.
