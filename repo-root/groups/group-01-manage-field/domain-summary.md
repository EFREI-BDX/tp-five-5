# Domain Summary - Field Management

**Business goal**

Manage playable fields, their availability statuses, reservations, and slot availability as a shared source of truth for other
groups.

**Ubiquitous language**

- **Field** - reservable field identified by `id`, `name`, `status_id`.
- **FieldStatus** - reference status of a field: `active`, `inactive`, `maintenance`.
- **ReservationStatus** - reference status of a reservation: `pending`, `confirmed`, `cancelled`.
- **Reservation** - reservation record for one field on a `date` between `start_time` and `end_time`.
- **TimeSlot** - value object representing a same-day reservation slot.
- **Event** - message published after creation or status change.

**Main business invariants**

- All `id` and `*_id` values are non-empty valid UUIDs.
- `field.name` is required and unique.
- `field.status_id` must reference an existing `field_status`.
- `reservation.status_id` must reference an existing `reservation_status`.
- `date` uses `YYYY-MM-DD`.
- `start_time` and `end_time` use `HH:MM` on full-hour or half-hour boundaries.
- Reservation duration must be `60`, `90`, or `120` minutes.
- Two reservations of the same field cannot overlap when their status is `pending` or `confirmed`.
- A `cancelled` reservation no longer blocks the slot.
- A reservation in this context only represents field occupancy, not player or team assignment.

**Main produced events**

- **FieldCreated** - `field_id`, `name`, `status_id`, `occurred_at`, `trace_id`
- **FieldStatusChanged** - `field_id`, `previous_status_id`, `status_id`, `occurred_at`, `trace_id`
- **ReservationCreated** - `reservation_id`, `field_id`, `status_id`, `date`, `start_time`, `end_time`, `occurred_at`, `trace_id`
- **ReservationStatusChanged** - `reservation_id`, `field_id`, `previous_status_id`, `status_id`, `occurred_at`, `trace_id`
