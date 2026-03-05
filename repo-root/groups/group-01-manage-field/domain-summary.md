# Domain Summary - Field Management

**Objectif metier**
Gerer le cycle de vie des terrains de five (creation, statut, suppression) et les reservations de creneaux, afin
d'exposer une source de verite unique pour la disponibilite des terrains et de publier des evenements de domaine
consommables par les autres contextes (booking, planning, notifications).

**Ubiquitous language**

- **Field** - entite representant un terrain (`fieldId`, `name`, `status`).
- **FieldStatus** - etat courant du terrain: `active`, `maintenance`, `unavailable`.
- **Reservation** - allocation d'un `TimeSlot` sur un terrain, identifiee par `reservationId`.
- **TimeSlot** - intervalle temporel (`startAt`, `endAt`) porte par la reservation.
- **Value Object (VO)** - objets immuables et sans identite: `TimeSlot`, payloads d'evenements.
- **Event** - message metier publie apres une transition de domaine (`FieldCreated`, `FieldStatusChanged`, etc.).
- **Command** - requete API synchrone initiant une intention metier (create field, change status, reserve slot).
- **Aggregate** - racine transactionnelle `Field`, responsable de la coherence locale statut/reservations.

**Principales invariants metier**

- **Field.fieldId** et **Reservation.reservationId** doivent etre des UUID valides.
- **Field.name** doit etre non vide, avec une regle metier supplementaire: pas uniquement des espaces.
- **Field.status** doit etre dans l'enumeration autorisee (`active`, `maintenance`, `unavailable`).
- **TimeSlot.startAt** et **TimeSlot.endAt** doivent etre au format `date-time` ISO-8601.
- **TimeSlot.endAt** doit etre strictement superieur a **TimeSlot.startAt**.
- Un evenement **FieldStatusChanged** doit respecter **previousStatus != newStatus**.
- Si **reason** est present dans un changement de statut, sa valeur doit etre non vide.

**Principaux events produits**

- **FieldCreated** - payload minimal: `fieldId`, `name`, `status`.
- **FieldDeleted** - payload minimal: `fieldId`.
- **FieldStatusChanged** - payload minimal: `fieldId`, `previousStatus`, `newStatus`, `reason?`.
- **FieldReservationCreated** - payload minimal: `reservationId`, `fieldId`, `slot`.
- **FieldReservationDeleted** - payload minimal: `reservationId`, `fieldId`.
