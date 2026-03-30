# TeamRenamed

**Résumé métier**

Cet évènement est émis lorsqu'une équipe change de nom.

Il permet d'indiquer qu'une équipe identifiée par son id a été renommée avec une nouvelle valeur de nom.

**Déclencheur**

- Une commande ou une action métier de renommage d'équipe est validée.
- Le changement est accepté uniquement si le nouveau nom respecte les invariants métier.

**Payload JSON**

- id — *VO Id*
- newName — *String*

**Invariants**

- id doit être non vide
- id doit être un UUID valide
- newName doit être non vide
- newName doit respecter les règles métier de nommage d'une équipe
- newName doit être différent de l'ancien nom si cette règle est retenue

**Format JSON attendu**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "newName": "Paris Five Stars"
}
