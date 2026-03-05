# Contexts Map - Field Management

**Boundary**
Source of truth: metadonnees des terrains de five (`fieldId`, `name`, `status`) et gestion des reservations de
creneaux (`reservationId`, `slot`).

**APIs exposees**

- POST /fields - creer un terrain (201 + payload `FieldCreated`)
- GET /fields/{fieldId} - recuperer le detail d'un terrain (200 + payload `Field`)
- PATCH /fields/{fieldId}/status - changer le statut d'un terrain (200 + payload `FieldStatusChanged`)
- POST /fields/{fieldId}/reservations - creer une reservation (201 + payload `FieldReservationCreated`)
- DELETE /fields/{fieldId}/reservations/{reservationId} - supprimer une reservation (200 + payload
  `FieldReservationDeleted`)
- DELETE /fields/{fieldId} - supprimer un terrain (200 + payload `FieldDeleted`)

**APIs consommees (references)**

- Aucune dependance synchrone obligatoire documentee a ce stade.
- Les integrations inter-contextes se font via la publication d'evenements de domaine.

**Invariants**

- `fieldId` et `reservationId` doivent etre des UUID valides.
- `status` doit appartenir a `active`, `maintenance`, `unavailable`.
- `name` doit etre non vide et ne peut pas etre compose uniquement d'espaces.
- `slot.startAt` et `slot.endAt` doivent etre au format `date-time` ISO-8601.
- `slot.endAt` doit etre strictement superieur a `slot.startAt`.
- Un changement de statut valide impose `previousStatus` different de `newStatus`.
