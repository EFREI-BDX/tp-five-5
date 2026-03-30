# Id

**Business summary**

Technical identifier shared by entities and cross-context references.

**JSON representation**

- object with one `id` property

**Invariants**

- `id` is required
- `id` cannot be empty
- `id` must be a valid UUID

**Expected JSON format**

- **Schema** : `tests/schemas/id.schema.json`
- **Valid fixture** : `tests/fixtures/id.valid.json`
- **Invalid fixture** : `tests/fixtures/id.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a non-empty UUID
- **createInvalidThrows** - rejects an empty or malformed UUID
- **jsonRoundtrip** - serialization preserves the value
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
