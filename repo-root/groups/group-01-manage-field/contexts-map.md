# Contexts Map - Manage Field

**Boundary**

Source of truth: metadonnees des terrains, statuts de reference et reservations de creneaux.

**APIs exposees**

- `GET /v1/terrain-statuses` - lister les statuts de terrain
- `GET /v1/reservation-statuses` - lister les statuts de reservation
- `POST /v1/terrains` - creer un terrain
- `GET /v1/terrains/{terrain_id}` - recuperer un terrain
- `PATCH /v1/terrains/{terrain_id}/status` - changer le statut d'un terrain
- `POST /v1/terrains/{terrain_id}/reservations` - creer une reservation
- `PATCH /v1/terrains/{terrain_id}/reservations/{reservation_id}/status` - changer le statut d'une reservation

**APIs consommees**

- Aucune dependance synchrone obligatoire documentee.
- Les autres groupes consomment prioritairement les events de domaine.

**Invariants**

- `name` est un `TerrainName` non vide.
- `status_id` doit pointer vers une donnee de reference existante.
- `start_at < end_at`.
- Pas de chevauchement de reservations actives sur un meme terrain.
