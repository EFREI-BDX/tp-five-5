# TerrainStatusCode

**Resume metier**

Code metier associe a un `terrain_status`.

**Representation JSON**

- `string` dans `active`, `inactive`, `maintenance`

**Invariants**

- la valeur est en minuscules
- la valeur appartient a l'enumeration autorisee

**Format JSON attendu**

- **Schema** : `tests/schemas/terrain-status-code.schema.json`
- **Fixture valide** : `tests/fixtures/terrain-status-code.valid.json`
- **Fixture invalide** : `tests/fixtures/terrain-status-code.invalid.json`

**Tests minimaux attendus**

- **createValid** - un code autorise est accepte
- **createInvalidThrows** - un code inconnu est rejete
- **jsonRoundtrip** - la serialisation conserve la valeur
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
