# reservation

**Business summary**

Reservation for one field on a same-day slot.

**Attributes**

- **id** - *UUID* - technical identifier
- **field_id** - *UUID* - reference to `field.id`
- **status_id** - *UUID* - reference to `reservation_status.id`
- **date** - *date* - reservation date
- **start_time** - *string* - reservation start time in `HH:MM`
- **end_time** - *string* - reservation end time in `HH:MM`

**Invariants**

- `field_id` references an existing `field`
- `status_id` references an existing `reservation_status`
- `date` uses `YYYY-MM-DD`
- `start_time` and `end_time` use full-hour or half-hour boundaries
- reservation duration is `60`, `90`, or `120` minutes
- no active overlap is allowed on the same field
- a `cancelled` reservation no longer blocks the field
- player, team, and match result ownership stay outside this entity

**Expected JSON format**

- **Schema** : `tests/schemas/reservation.schema.json`
- **Valid fixture** : `tests/fixtures/reservation.valid.json`
- **Invalid fixture** : `tests/fixtures/reservation.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a valid reservation
- **createInvalidThrows** - rejects invalid date, time, or duration values
- **rejectOverlap** - rejects `pending` or `confirmed` overlap
- **allowCancelledOverlap** - accepts overlap with a `cancelled` reservation
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
