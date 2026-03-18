# Entité `terrain`

## Attributs

| Nom         | Type   | Obligatoire | Rôle                               |
| ----------- | ------ | ----------: | ---------------------------------- |
| `id`        | UUID   |         oui | identifiant technique              |
| `name`      | string |         oui | nom du terrain                     |
| `status_id` | UUID   |         oui | référence vers `terrain_status.id` |

## Invariants

- `id` est unique.
- `name` est obligatoire.
- `name` est unique.
- `status_id` doit exister dans `terrain_status`.

## Format JSON attendu

### Schéma
```json
{
  "id": "uuid",
  "name": "string",
  "status_id": "uuid"
}
```

### Fixture valide
```json
{
  "id": "22222222-2222-4222-8222-222222222222",
  "name": "Terrain A",
  "status_id": "11111111-1111-4111-8111-111111111111"
}
```

### Fixture invalide
```json
{
  "id": "22222222-2222-4222-8222-222222222222",
  "name": "",
  "status_id": "unknown-status-id"
}
```

## Tests minimaux attendus

- accepte un terrain valide
- refuse un nom vide
- refuse un nom dupliqué
- refuse un `status_id` inexistant

## Génération des fixtures

- générer un UUID
- générer un nom métier simple : `Terrain A`, `Terrain B`
- réutiliser un `status_id` existant
