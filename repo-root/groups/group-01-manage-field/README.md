# group-01-manage-field

**Contexte**

Source de verite pour le catalogue des terrains de five et les reservations associees.

**Membres**

- **Nom Prenom** - identifiant GitHub

## Perimetre

- `terrain_status`
- `reservation_status`
- `terrain`
- `terrain_reservation`

## Livrables

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

- Les statuts sont des donnees de reference.
- Deux reservations actives ne se chevauchent pas sur un meme terrain.
