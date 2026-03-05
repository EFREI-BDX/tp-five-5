# Domain Summary - Field Management

**Objectif metier**
Gerer les terrains de five (creation, mise a jour, disponibilite et reservation) pour permettre la programmation
des matchs et la coherence des donnees partagees avec les autres bounded contexts.

**Ubiquitous language**

- **Field** - entite representant un terrain (id, nom, statut, surface, adresse).
- **Reservation** - bloc de disponibilite associe a un terrain et un match.
- **TimeSlot** - Value Object immuable qui decrit un creneau temporel (`startAt`, `endAt`).
- **FieldSurface** - VO immuable qui contraint le type de surface.
- **FieldStatus** - etat operationnel d'un terrain (`active`, `maintenance`, `unavailable`).
- **Event** - message metier publie sur le bus (`FieldCreated`, `FieldStatusChanged`, `FieldReservationCreated`).
- **Command** - requete synchrone API (`CreateField`, `UpdateFieldStatus`, `ReserveField`).

**Principales invariants metier**

- **Field.id** doit etre un UUID unique.
- **Field.name** et **Field.address** ne peuvent pas etre vides.
- **FieldSurface.surface** doit appartenir a l'enumeration autorisee.
- Les reservations d'un meme terrain ne doivent jamais se chevaucher.
- Un terrain non `active` ne peut pas recevoir de nouvelle reservation.
- Les events publies doivent contenir **timestamp** ISO-8601 et **traceId**.

**Dependances potentielles inter-groupes**

- **Group 02 - Manage Match**: verifie la disponibilite et reserve/libere des creneaux; consomme `FieldStatusChanged`.
- **Group 03 - Record Match**: lit les metadonnees terrain et peut publier `MatchRecorded` pour statistiques d'usage.
- **Group 04 - Summarize Match**: lit les details terrain pour enrichir les resumes de match.
- **Group 05 - Manage Player**: consulte le catalogue de terrains (filtre geographique/surface) pour preferences joueurs.
- **Group 06 - Manage Team**: consulte les terrains eligibles pour planification d'equipe ou terrain habituel.

**Principaux events produits**

- **FieldCreated** - payload minimal: `fieldId`, `name`, `status`, `timestamp`, `traceId`.
- **FieldStatusChanged** - payload minimal: `fieldId`, `previousStatus`, `newStatus`, `timestamp`, `traceId`.
- **FieldReservationCreated** - payload minimal: `reservationId`, `fieldId`, `matchId`, `slot`, `timestamp`, `traceId`.

**Principaux events consommes (potentiels)**

- **MatchScheduled** (Group 02) - reserve un creneau terrain.
- **MatchCancelled** (Group 02) - libere un creneau terrain.
- **MatchRecorded** (Group 03) - met a jour les donnees de charge/utilisation terrain.
