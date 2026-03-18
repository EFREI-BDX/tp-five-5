# Contexts Map - Record Match

**Boundary**
Source of truth : enregistrement des événements d'un match (buts, fautes, cartons, début et fin de match).

**APIs exposées**

- `POST /v1/matches` — démarre un nouveau match (201 `{ id, startTime }`)
- `GET /v1/matches/{id}` — récupère les informations d'un match et la liste de ses événements
- `POST /v1/matches/{id}/events` — enregistre un événement sur un match en cours (201 `{ eventId }`)
- `GET /v1/matches/{id}/events` — liste tous les événements d'un match

**APIs consommées (références)**

- Player API (groupe 05) — `GET /v1/players/{id}` — pour valider qu'un joueur acteur/victime existe
- Team API (groupe 06) — `GET /v1/teams/{id}` — pour valider que les équipes participantes existent
- Match API (groupe 02) — `GET /v1/matches/{id}` — pour récupérer les informations de planification d'un match

**Invariants**

- `match.startTime` est un datetime ISO-8601 valide
- `event.timestamp >= match.startTime`
- `event.action` ∈ `{ goal, foul, yellowCard, redCard, kickOff, endMatch }`
