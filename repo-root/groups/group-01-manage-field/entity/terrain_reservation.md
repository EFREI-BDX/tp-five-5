# Entité `terrain_reservation`

## Attributs

| Nom          | Type              | Obligatoire | Rôle                                   |
| ------------ | ----------------- | ----------: | -------------------------------------- |
| `id`         | UUID              |         oui | identifiant technique                  |
| `terrain_id` | UUID              |         oui | référence vers `terrain.id`            |
| `status_id`  | UUID              |         oui | référence vers `reservation_status.id` |
| `start_at`   | datetime ISO 8601 |         oui | début de réservation                   |
| `end_at`     | datetime ISO 8601 |         oui | fin de réservation                     |

## Invariants

- `id` est unique.
- `terrain_id` doit exister dans `terrain`.
- `status_id` doit exister dans `reservation_status`.
- `start_at` < `end_at`.
- deux réservations du même terrain ne peuvent pas se chevaucher si leur statut est `pending` ou `confirmed`.
- une réservation `cancelled` n'occupe plus le créneau.

## Format JSON attendu

### Schéma
```json
{
  "id": "uuid",
  "terrain_id": "uuid",
  "status_id": "uuid",
  "start_at": "2026-03-18T10:00:00Z",
  "end_at": "2026-03-18T12:00:00Z"
}
```

### Fixture valide
```json
{
  "id": "44444444-4444-4444-8444-444444444444",
  "terrain_id": "22222222-2222-4222-8222-222222222222",
  "status_id": "33333333-3333-4333-8333-333333333333",
  "start_at": "2026-03-18T10:00:00Z",
  "end_at": "2026-03-18T12:00:00Z"
}
```

### Fixture invalide
```json
{
  "id": "44444444-4444-4444-8444-444444444444",
  "terrain_id": "22222222-2222-4222-8222-222222222222",
  "status_id": "33333333-3333-4333-8333-333333333333",
  "start_at": "2026-03-18T12:00:00Z",
  "end_at": "2026-03-18T10:00:00Z"
}
```

## Tests minimaux attendus

- accepte une réservation valide
- refuse `start_at >= end_at`
- refuse un chevauchement sur le même terrain avec une réservation `pending`
- refuse un chevauchement sur le même terrain avec une réservation `confirmed`
- accepte un chevauchement si l'autre réservation est `cancelled`
- refuse un `terrain_id` inexistant
- refuse un `status_id` inexistant

## Génération des fixtures

- générer un UUID
- réutiliser un `terrain_id` existant
- réutiliser un `status_id` existant
- générer `start_at` et `end_at` cohérents
- prévoir au moins un cas `confirmed` et un cas `cancelled`
