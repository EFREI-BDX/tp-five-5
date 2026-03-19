# Contexts Map - Field Management

**Boundary**

Source of truth for field metadata, reference statuses, reservations, and slot availability.

**Exposed APIs**

- `GET /v1/field-statuses` - list field statuses
- `GET /v1/reservation-statuses` - list reservation statuses
- `POST /v1/fields` - create a field
- `GET /v1/fields/available` - list fields available for a requested slot
- `GET /v1/fields/{field_id}` - get one field
- `PATCH /v1/fields/{field_id}/status` - change a field status
- `POST /v1/fields/{field_id}/reservations` - create a reservation
- `PATCH /v1/fields/{field_id}/reservations/{reservation_id}/status` - change a reservation status

**Direct relations with other groups**

- `group-02-manage-match` - direct synchronous consumer of field lookup, availability, and reservation APIs.
- `group-03-record-match` - no direct synchronous relation expected.
- `group-04-summarize-match` - no direct synchronous relation expected.
- `group-05-manage-player` - owns player data outside this context.
- `group-06-manage-team` - owns team data outside this context.

**Consumed APIs**

- No mandatory synchronous dependency is documented for Field Management itself.

**Invariants**

- `name` is a non-empty `FieldName`.
- `status_id` must point to existing reference data.
- `date`, `start_time`, and `end_time` define a same-day slot.
- `start_time` and `end_time` must align on full hours or half hours.
- Reservation duration must be `60`, `90`, or `120` minutes.
- Active reservations cannot overlap on the same field.
- `reservation` does not model player, team, or match result ownership.
