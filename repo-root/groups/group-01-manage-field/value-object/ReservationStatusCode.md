# ReservationStatusCode

**Resume metier**

Code metier associe a un `reservation_status`.

**Representation JSON**

- `string` dans `pending`, `confirmed`, `cancelled`

**Invariants**

- la valeur est en minuscules
- la valeur appartient a l'enumeration autorisee

**Format JSON attendu**

- **Schema** : `tests/schemas/reservation-status-code.schema.json`
- **Fixture valide** : `tests/fixtures/reservation-status-code.valid.json`
- **Fixture invalide** : `tests/fixtures/reservation-status-code.invalid.json`

**Tests minimaux attendus**

- **createValid** - un code autorise est accepte
- **createInvalidThrows** - un code inconnu est rejete
- **jsonRoundtrip** - la serialisation conserve la valeur
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
