# terrain_reservation

**Resume metier**

Reservation d'un terrain sur un creneau donne.

**Attributs**

- **id** - *UUID* - identifiant technique
- **terrain_id** - *UUID* - reference vers `terrain.id`
- **status_id** - *UUID* - reference vers `reservation_status.id`
- **start_at** - *date-time* - debut de reservation
- **end_at** - *date-time* - fin de reservation

**Invariants**

- `terrain_id` reference un `terrain` existant
- `status_id` reference un `reservation_status` existant
- `start_at < end_at`
- pas de chevauchement actif sur un meme terrain
- une reservation `cancelled` ne bloque plus le creneau

**Format JSON attendu**

- **Schema** : `tests/schemas/terrain-reservation.schema.json`
- **Fixture valide** : `tests/fixtures/terrain-reservation.valid.json`
- **Fixture invalide** : `tests/fixtures/terrain-reservation.invalid.json`

**Tests minimaux attendus**

- **createValid** - une reservation valide est acceptee
- **createInvalidThrows** - des dates invalides sont rejetees
- **rejectOverlap** - un chevauchement `pending` ou `confirmed` est refuse
- **allowCancelledOverlap** - un chevauchement avec une reservation `cancelled` est accepte
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
