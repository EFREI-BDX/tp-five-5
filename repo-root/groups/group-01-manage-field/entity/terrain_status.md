# terrain_status

**Resume metier**

Donnee de reference pour le statut d'un terrain.

**Attributs**

- **id** - *UUID* - identifiant technique
- **code** - *string* - `active`, `inactive`, `maintenance`
- **label** - *string* - libelle lisible

**Invariants**

- `id` est unique
- `code` est unique
- `code` est en minuscules
- `code` appartient a l'enumeration autorisee

**Format JSON attendu**

- **Schema** : `tests/schemas/terrain-status.schema.json`
- **Fixture valide** : `tests/fixtures/terrain-status.valid.json`
- **Fixture invalide** : `tests/fixtures/terrain-status.invalid.json`

**Tests minimaux attendus**

- **createValid** - un statut valide est accepte
- **createInvalidThrows** - un UUID ou un code invalide est rejete
- **jsonRoundtrip** - la serialisation conserve les champs
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
