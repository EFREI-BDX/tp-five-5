# reservation_status

**Resume metier**

Donnee de reference pour le statut d'une reservation.

**Attributs**

- **id** - *UUID* - identifiant technique
- **code** - *string* - `pending`, `confirmed`, `cancelled`
- **label** - *string* - libelle lisible

**Invariants**

- `id` est unique
- `code` est unique
- `code` est en minuscules
- `code` appartient a l'enumeration autorisee

**Format JSON attendu**

- **Schema** : `tests/schemas/reservation-status.schema.json`
- **Fixture valide** : `tests/fixtures/reservation-status.valid.json`
- **Fixture invalide** : `tests/fixtures/reservation-status.invalid.json`

**Tests minimaux attendus**

- **createValid** - un statut valide est accepte
- **createInvalidThrows** - un code invalide est rejete
- **jsonRoundtrip** - la serialisation conserve les champs
- **schemaValidation** - la fixture valide passe, la fixture invalide echoue
