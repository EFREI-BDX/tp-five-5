# TimeSlot

**Resume metier**

Creneau de reservation porte par `terrain_reservation`.

**Attributs**

- **start_at** - *date-time* - debut du creneau
- **end_at** - *date-time* - fin du creneau

**Invariants**

- `start_at` et `end_at` sont au format ISO 8601
- `start_at < end_at`

**Format JSON attendu**

- **Schema** : `tests/schemas/time-slot.schema.json`
- **Fixture valide** : `tests/fixtures/time-slot.valid.json`
- **Fixture invalide** : `tests/fixtures/time-slot.invalid.json`

**Tests minimaux attendus**

- **createValid** - un creneau coherent est accepte
- **createInvalidThrows** - `start_at >= end_at` est rejete
- **jsonRoundtrip** - la serialisation conserve les dates
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
