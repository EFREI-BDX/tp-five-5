# TeamNameUpdated

**Résumé métier**

Cet évènement est émis lorsqu'une équipe change de nom.

Il permet d'indiquer qu'une équipe identifiée par son id a changé son nom.

**Déclencheur**

- Une commande ou une action métier de modification du nom d'une équipe est validée.
- La modification est acceptée uniquement si les invariants métier de l'équipe sont respectés.

**Payload JSON**

- teamId — *VO Id*
- name — *String*

**Invariants**

- teamId doit être non vide
- teamId doit être un UUID valide
- name doit être non vide
- name doit respecter le format défini (entre 2 et 100 caractères)
- teamId doit correspondre à une équipe existante

**Format JSON attendu**

```json
{
  "teamId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Paris Saint-Germain"
}
```

- **Schéma** : `tests/schemas/TeamNameUpdated.schema.json`
- **Fixture valide** : `tests/fixtures/TeamNameUpdated.valid.json`
- **Fixture invalide** : `tests/fixtures/TeamNameUpdated.invalid.json`

**Producteur**

- Aggregate / service métier de gestion d'équipe

**Consommateurs possibles**

- journalisation métier
- projection de lecture
- audit
- synchronisation avec un autre système

**Tests minimaux attendus**

- vérifier qu'un payload avec teamId et name valides est accepté
- vérifier qu'un payload avec un teamId invalide est rejeté
- vérifier qu'un payload avec un name vide est rejeté
- vérifier qu'un payload avec un identifiant vide est rejeté

**Remarques**

Cet évènement ne représente que le fait métier "une équipe a changé de nom".
Il ne contient volontairement que les données utiles à ce fait.
