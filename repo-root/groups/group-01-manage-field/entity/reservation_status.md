# reservation_status

**Business summary**

Reference data for a reservation status.

**Attributes**

- **id** - *UUID* - technical identifier
- **code** - *string* - `pending`, `confirmed`, `cancelled`
- **label** - *string* - display label

**Invariants**

- `id` is unique
- `code` is unique
- `code` is lowercase
- `code` belongs to the allowed enumeration

**Expected JSON format**

- **Schema** : `tests/schemas/reservation-status.schema.json`
- **Valid fixture** : `tests/fixtures/reservation-status.valid.json`
- **Invalid fixture** : `tests/fixtures/reservation-status.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a valid status
- **createInvalidThrows** - rejects an invalid code
- **jsonRoundtrip** - serialization preserves fields
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
