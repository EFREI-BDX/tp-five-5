# field

**Business summary**

Reservable field from the catalog.

**Attributes**

- **id** - *UUID* - technical identifier
- **name** - *string* - business field name
- **status_id** - *UUID* - reference to `field_status.id`

**Invariants**

- `id` is unique
- `name` is required
- `name` is unique
- `status_id` references an existing `field_status`

**Expected JSON format**

- **Schema** : `tests/schemas/field.schema.json`
- **Valid fixture** : `tests/fixtures/field.valid.json`
- **Invalid fixture** : `tests/fixtures/field.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a valid field
- **createInvalidThrows** - rejects an empty name or invalid `status_id`
- **jsonRoundtrip** - serialization preserves fields
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
