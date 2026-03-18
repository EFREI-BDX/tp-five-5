# Entité `terrain_status`

## Attributs

| Nom     | Type   | Obligatoire | Rôle                  |
| ------- | ------ | ----------: | --------------------- |
| `id`    | UUID   |         oui | identifiant technique |
| `code`  | string |         oui | code unique du statut |
| `label` | string |         oui | libellé lisible       |

## Invariants

- `id` est unique.
- `code` est unique.
- `code` est en minuscules.
- Valeurs : `active`, `inactive`, `maintenance`.

## Format JSON attendu

### Schéma
```json
{
  "id": "uuid",
  "code": "string",
  "label": "string"
}
```

### Fixture valide
```json
{
  "id": "11111111-1111-4111-8111-111111111111",
  "code": "active",
  "label": "Actif"
}
```

### Fixture invalide
```json
{
  "id": "not-a-uuid",
  "code": "ACTIVE",
  "label": "Actif"
}
```

## Tests minimaux attendus

- accepte un UUID valide
- refuse un UUID invalide
- refuse un `code` dupliqué
- refuse un `code` en majuscules si la règle impose minuscules

## Génération des fixtures

- générer un UUID
- choisir un `code` métier connu
- renseigner un `label` lisible
