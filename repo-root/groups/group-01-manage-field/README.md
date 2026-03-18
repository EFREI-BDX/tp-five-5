# group-01-manage-field

**Context**

Source of truth for the five-a-side field catalog and related reservations.

**Members**

- **Full Name** - GitHub handle

## Scope

- `field_status`
- `reservation_status`
- `field`
- `field_reservation`

## Deliverables

- `domain-summary.md`
- `contexts-map.md`
- `contexts-map.puml`
- `entity/*.md`
- `value-object/*.md`
- `openapi.yaml`
- `service-declaration.json`
- `mock/postman-collection.json`
- `tests/schemas/*.schema.json`
- `tests/fixtures/*.json`
- `CONTRIBUTION.md`

**Notes**

- Statuses are reference data.
- Two active reservations cannot overlap on the same field.
- This context does not own players, teams, matches, or match results.
