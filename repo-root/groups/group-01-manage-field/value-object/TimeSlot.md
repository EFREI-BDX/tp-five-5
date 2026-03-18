# TimeSlot

**Business summary**

Reservation slot carried by `field_reservation`.

**Attributes**

- **start_at** - *date-time* - slot start
- **end_at** - *date-time* - slot end

**Invariants**

- `start_at` and `end_at` use ISO 8601 format
- `start_at < end_at`

**Expected JSON format**

- **Schema** : `tests/schemas/time-slot.schema.json`
- **Valid fixture** : `tests/fixtures/time-slot.valid.json`
- **Invalid fixture** : `tests/fixtures/time-slot.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a valid slot
- **createInvalidThrows** - rejects `start_at >= end_at`
- **jsonRoundtrip** - serialization preserves the dates
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
