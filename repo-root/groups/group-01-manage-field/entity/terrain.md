# terrain

**Resume metier**

Terrain reservable du catalogue.

**Attributs**

- **id** - *UUID* - identifiant technique
- **name** - *string* - nom metier du terrain
- **status_id** - *UUID* - reference vers `terrain_status.id`

**Invariants**

- `id` est unique
- `name` est obligatoire
- `name` est unique
- `status_id` reference un `terrain_status` existant

**Format JSON attendu**

- **Schema** : `tests/schemas/terrain.schema.json`
- **Fixture valide** : `tests/fixtures/terrain.valid.json`
- **Fixture invalide** : `tests/fixtures/terrain.invalid.json`

**Tests minimaux attendus**

- **createValid** - un terrain valide est accepte
- **createInvalidThrows** - un nom vide ou un `status_id` invalide est rejete
- **jsonRoundtrip** - la serialisation conserve les champs
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
