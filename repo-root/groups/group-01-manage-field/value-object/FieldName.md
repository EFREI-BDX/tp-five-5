# FieldName

**Business summary**

Business name of a field.

**JSON representation**

- object with one `name` property

**Invariants**

- `name` is required
- `name` cannot contain only spaces

**Expected JSON format**

- **Schema** : `tests/schemas/field-name.schema.json`
- **Valid fixture** : `tests/fixtures/field-name.valid.json`
- **Invalid fixture** : `tests/fixtures/field-name.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a non-empty name
- **createBlankThrows** - rejects an empty or blank name
- **jsonRoundtrip** - serialization preserves the value
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
