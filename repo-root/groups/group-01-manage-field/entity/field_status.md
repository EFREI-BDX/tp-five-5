# field_status

**Business summary**

Reference data for a field status.

**Attributes**

- **id** - *UUID* - technical identifier
- **code** - *string* - `active`, `inactive`, `maintenance`
- **label** - *string* - display label

**Invariants**

- `id` is unique
- `code` is unique
- `code` is lowercase
- `code` belongs to the allowed enumeration

**Expected JSON format**

- **Schema** : `tests/schemas/field-status.schema.json`
- **Valid fixture** : `tests/fixtures/field-status.valid.json`
- **Invalid fixture** : `tests/fixtures/field-status.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a valid status
- **createInvalidThrows** - rejects an invalid UUID or code
- **jsonRoundtrip** - serialization preserves fields
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
