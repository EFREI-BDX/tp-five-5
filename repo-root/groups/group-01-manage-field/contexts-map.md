# Contexts Map - Field Management

**Boundary**
Source of truth: catalogue des terrains, statut operationnel et non-chevauchement des reservations.

**APIs exposees**

- POST /fields - cree un terrain (201 { id })
- GET /fields - liste et filtre les terrains
- GET /fields/{fieldId} - detail d'un terrain
- PATCH /fields/{fieldId}/status - met a jour le statut terrain
- GET /fields/{fieldId}/availability - retourne disponibilite sur une periode
- POST /fields/{fieldId}/reservations - reserve un creneau pour un match
- DELETE /fields/{fieldId}/reservations/{reservationId} - libere une reservation

**APIs consommees (potentielles)**

- Group 02 / Manage Match events - `MatchScheduled`, `MatchCancelled`
- Group 03 / Record Match events - `MatchRecorded`
- Group 06 / Manage Team API - GET /teams/{id} (si validation ownership terrain)

**Dependances descendantes (consommateurs de ce service)**

- Group 02 / Manage Match - verification disponibilite et reservation
- Group 03 / Record Match - lecture metadonnees terrain
- Group 04 / Summarize Match - enrichissement des resumes
- Group 05 / Manage Player - consultation catalogue terrains
- Group 06 / Manage Team - consultation et rattachement terrain

**Invariants**

- field.status in {active, maintenance, unavailable}
- field.name is NonEmptyString
- reservation slots for one field must not overlap
