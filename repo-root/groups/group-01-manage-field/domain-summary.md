# Domain Summary - Field Management

**Business goal**

Manage playable fields, their availability statuses, reservations, and slot availability as a shared source of truth for other
groups.

**Ubiquitous language**

- **Field** - reservable field identified by `id`, `name`, `status_id`; detailed reads include the field status object.
- **FieldStatus** - reference status of a field: `active`, `inactive`, `maintenance`.
- **ReservationStatus** - reference status of a reservation: `pending`, `confirmed`, `cancelled`.
- **Reservation** - reservation record for one field on a `date` between `start_time` and `end_time`; reads include the reservation status object.
- **TimeSlot** - value object representing a same-day reservation slot.

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

**Domain Model**

- `Field` is a pure domain aggregate, independent from JPA.
- `Reservation` is a pure domain aggregate, independent from JPA.
- `DomainId`, `FieldName`, and `TimeSlot` are pure value objects.
- `FieldAvailabilityDomainService` computes available fields from field aggregates, reservation aggregates, and a requested slot.
- `ReservationPolicy` protects reservation creation invariants before persistence.

**Application Architecture**

- Application services depend on ports, not directly on Spring Data repositories.
- Persistence adapters implement these ports with JPA read views and stored procedures.
- SQL procedure return codes are translated in the procedural persistence package, not in the domain model.

**Persistence and Read Models**

- The application user reads only views and executes procedures for writes.
- `v_field_details` joins fields with their field status.
- `v_active_field` exposes only fields eligible for availability checks.
- `v_reservation_details` joins reservations with their reservation status.
- `v_blocking_reservation` exposes only pending or confirmed reservations that can block a slot.
- No domain events are emitted by the Kotlin application at this stage.
