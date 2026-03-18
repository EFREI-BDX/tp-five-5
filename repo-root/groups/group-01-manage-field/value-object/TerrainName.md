# TerrainName

**Resume metier**

Nom metier d'un terrain.

**Representation JSON**

- `string` non vide et non blanche

**Invariants**

- la valeur est obligatoire
- la valeur ne contient pas uniquement des espaces

**Format JSON attendu**

- **Schema** : `tests/schemas/terrain-name.schema.json`
- **Fixture valide** : `tests/fixtures/terrain-name.valid.json`
- **Fixture invalide** : `tests/fixtures/terrain-name.invalid.json`

**Tests minimaux attendus**

- **createValid** - une valeur non vide est acceptee
- **createBlankThrows** - une valeur vide ou blanche est rejetee
- **jsonRoundtrip** - la serialisation conserve la valeur
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
