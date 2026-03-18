# EquipmentSlot

**Résumé métier**  
Identifie l'emplacement où un item peut être équipé par un personnage.

**Attributs**

- **slot** — *string* — valeur autorisée : **head, body, mainHand, offHand, legs, feet**

**Invariants**

- **slot** doit appartenir à l'énumération autorisée.

**Format JSON attendu**

- **Schéma** : `tests/schemas/equipment-slot.schema.json`
- **Fixture valide** : `tests/fixtures/equipment-slot.valid.json`
- **Fixture invalide** : `tests/fixtures/equipment-slot.invalid.json`

**Tests minimaux attendus**

- **createValid** — construction avec une valeur d'énumération valide ne lève pas d'exception.
- **createInvalidThrows** — valeur hors énumération lève une exception métier.
- **jsonRoundtrip** — sérialisation/désérialisation conserve la valeur.
- **schemaValidation** — fixture valide valide le schema ; fixture invalide échoue.

**Génération des fixtures**

- Documenter la commande ou le test qui produit les fixtures.

