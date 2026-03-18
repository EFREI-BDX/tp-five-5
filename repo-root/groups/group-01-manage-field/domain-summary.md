# Domain Summary - Field Management

**Business goal**

Manage playable fields, their availability statuses, and reservation slots as a shared source of truth for other
groups.

**Ubiquitous language**

- **Field** - reservable field identified by `id`, `name`, `status_id`.
- **FieldStatus** - reference status of a field: `active`, `inactive`, `maintenance`.
- **ReservationStatus** - reference status of a reservation: `pending`, `confirmed`, `cancelled`.
- **FieldReservation** - slot occupancy record for one field over a `start_at` / `end_at` interval.
- **TimeSlot** - value object representing a reservation slot.
- **Event** - message published after creation or status change.

**Out of scope**

- Player identity and profile management.
- Team membership and roster management.
- Match composition, score recording, and match summaries.
- Any direct ownership of player-to-reservation or team-to-reservation relations.

**Main business invariants**

- All `id` and `*_id` values are valid UUIDs.
- `field.name` is required and unique.
- `field.status_id` must reference an existing `field_status`.
- `field_reservation.status_id` must reference an existing `reservation_status`.
- `start_at` must be strictly earlier than `end_at`.
- Two reservations of the same field cannot overlap when their status is `pending` or `confirmed`.
- A `cancelled` reservation no longer blocks the slot.
- A reservation in this context only represents field slot occupancy, not player or team assignment.

**Main produced events**

- **FieldCreated** - `field_id`, `name`, `status_id`, `occurred_at`, `trace_id`
- **FieldStatusChanged** - `field_id`, `previous_status_id`, `status_id`, `occurred_at`, `trace_id`
- **FieldReservationCreated** - `reservation_id`, `field_id`, `status_id`, `start_at`, `end_at`, `occurred_at`, `trace_id`
- **FieldReservationStatusChanged** - `reservation_id`, `field_id`, `previous_status_id`, `status_id`, `occurred_at`, `trace_id`

**Known inter-group relation**

- `group-02-manage-match` is the main synchronous consumer of field availability and slot reservation APIs.
- `group-03-record-match` and `group-04-summarize-match` should depend on match data, not on direct field ownership.
- `group-05-manage-player` and `group-06-manage-team` own player and team identities outside this bounded context.
