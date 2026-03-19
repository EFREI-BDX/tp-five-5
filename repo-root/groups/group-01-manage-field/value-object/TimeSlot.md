# TimeSlot

**Business summary**

Same-day reservation slot carried by `reservation`.

**Attributes**

- **date** - *date* - slot date
- **start_time** - *string* - slot start time in `HH:MM`
- **end_time** - *string* - slot end time in `HH:MM`

**Invariants**

- `date` uses `YYYY-MM-DD`
- `start_time` and `end_time` use full-hour or half-hour boundaries
- the slot stays on a single date
- allowed durations are `60`, `90`, or `120` minutes

**Expected JSON format**

- **Schema** : `tests/schemas/time-slot.schema.json`
- **Valid fixture** : `tests/fixtures/time-slot.valid.json`
- **Invalid fixture** : `tests/fixtures/time-slot.invalid.json`

**Minimum expected tests**

- **createValid** - accepts a valid slot
- **createInvalidThrows** - rejects invalid time boundaries or durations
- **jsonRoundtrip** - serialization preserves the slot
- **schemaValidation** - the valid fixture passes, the invalid fixture fails
