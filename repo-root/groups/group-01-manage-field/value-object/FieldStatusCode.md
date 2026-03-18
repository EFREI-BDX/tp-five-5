# FieldStatusCode

**Business summary**

Business code carried by `field_status`.

**JSON representation**

- object with one `code` property in `active`, `inactive`, `maintenance`

**Invariants**

- `code` is lowercase
- `code` belongs to the allowed enumeration

**Expected JSON format**

- **Schema** : `tests/schemas/field-status-code.schema.json`
- **Valid fixture** : `tests/fixtures/field-status-code.valid.json`
- **Invalid fixture** : `tests/fixtures/field-status-code.invalid.json`

**Minimum expected tests**

- **createValid** - accepts an allowed code
- **createInvalidThrows** - rejects an unknown code
- **jsonRoundtrip** - serialization preserves the value
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
