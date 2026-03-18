# Contexts Map - Character Management

**Boundary**
Source of truth: Character metadata (id, name, stats).

**APIs exposées**

- POST /v1/characters — crée un Character (201 { id })
- GET /v1/characters/{id} — résumé profil + inventaire

**APIs consommées (références)**

- Player API — GET /v1/players/{id}

**Invariants**

- inventoryQuantity >= 0
- character.name is NonEmptyString
