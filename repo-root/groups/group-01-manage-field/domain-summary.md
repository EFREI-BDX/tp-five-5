# Domain Summary - Manage Field

**Objectif metier**

Gerer les terrains, leurs statuts de disponibilite et les reservations de creneaux afin de fournir une source de
verite partagee aux autres groupes.

**Ubiquitous language**

- **Terrain** - terrain reservable identifie par `id`, `name`, `status_id`.
- **TerrainStatus** - statut de reference d'un terrain: `active`, `inactive`, `maintenance`.
- **ReservationStatus** - statut de reference d'une reservation: `pending`, `confirmed`, `cancelled`.
- **TerrainReservation** - reservation d'un terrain sur un intervalle `start_at` / `end_at`.
- **TimeSlot** - value object representant un creneau temporel.
- **Event** - message publie apres creation ou changement de statut.

**Principales invariants metier**

- Tous les `id` et `*_id` sont des UUID valides.
- `terrain.name` est obligatoire et unique.
- `terrain.status_id` doit referencer un `terrain_status` existant.
- `terrain_reservation.status_id` doit referencer un `reservation_status` existant.
- `start_at` doit etre strictement inferieur a `end_at`.
- Deux reservations du meme terrain ne se chevauchent pas si leur statut est `pending` ou `confirmed`.
- Une reservation `cancelled` n'occupe plus le creneau.

**Principaux events produits**

- **TerrainCreated** - `terrain_id`, `name`, `status_id`, `occurred_at`, `trace_id`
- **TerrainStatusChanged** - `terrain_id`, `previous_status_id`, `status_id`, `occurred_at`, `trace_id`
- **TerrainReservationCreated** - `reservation_id`, `terrain_id`, `status_id`, `start_at`, `end_at`, `occurred_at`, `trace_id`
- **TerrainReservationStatusChanged** - `reservation_id`, `terrain_id`, `previous_status_id`, `status_id`, `occurred_at`, `trace_id`
