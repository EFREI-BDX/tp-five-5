# Entité `reservation_status`

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
- Valeurs : `pending`, `confirmed`, `cancelled`.

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
  "id": "33333333-3333-4333-8333-333333333333",
  "code": "confirmed",
  "label": "Confirmée"
}
```

### Fixture invalide
```json
{
  "id": "33333333-3333-4333-8333-333333333333",
  "code": "CONFIRMED",
  "label": "Confirmée"
}
```

## Tests minimaux attendus

- accepte un statut valide
- refuse un `code` dupliqué
- refuse un `code` hors format attendu

## Génération des fixtures

- générer les fixtures de référence une seule fois
- prévoir au minimum : `pending`, `confirmed`, `cancelled`
