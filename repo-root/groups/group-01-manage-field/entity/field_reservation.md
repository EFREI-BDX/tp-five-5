# field_reservation

**Business summary**

Slot occupancy record for one field over a given slot.

**Attributes**

- **id** - *UUID* - technical identifier
- **field_id** - *UUID* - reference to `field.id`
- **status_id** - *UUID* - reference to `reservation_status.id`
- **start_at** - *date-time* - reservation start
- **end_at** - *date-time* - reservation end

**Invariants**

- `field_id` references an existing `field`
- `status_id` references an existing `reservation_status`
- `start_at < end_at`
- no active overlap is allowed on the same field
- a `cancelled` reservation no longer blocks the slot
- player, team, and match result ownership stay outside this entity

**Expected JSON format**

- **Schema** : `tests/schemas/field-reservation.schema.json`
- **Valid fixture** : `tests/fixtures/field-reservation.valid.json`
- **Invalid fixture** : `tests/fixtures/field-reservation.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a valid reservation
- **createInvalidThrows** - rejects invalid dates
- **rejectOverlap** - rejects `pending` or `confirmed` overlap
- **allowCancelledOverlap** - accepts overlap with a `cancelled` reservation
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
