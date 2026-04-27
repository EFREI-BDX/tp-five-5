# Team

**Résumé métier**

Représentation locale d'une équipe de five dans le contexte Record Match. Une Team n'est pas gérée ici, mais elle est référencée pour identifier les deux équipes participant à un match.

**Attributs**

- **id** - identifiant unique de l'équipe (UUID fourni par le groupe Manage Team)
- **tag** - acronyme de 3 caractères identifiant l'équipe (ex. `PSG`)

**Invariants**

- **id** doit être un UUID valide et non vide
- **tag** doit être une chaîne de 3 lettres majuscules et/ou chiffres

**Format JSON attendu**

- **Schéma** : `tests/schemas/team.schema.json`
- **Fixture valide** : `tests/fixtures/team.valid.json`
- **Fixture invalide** : `tests/fixtures/team.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un id UUID valide et un tag valide ne lève pas d'exception.
- **createInvalidIdThrows** - id non UUID lève une exception métier.
- **createInvalidTagThrows** - tag de longueur différente de 3 ou contenant des caractères non autorisés lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.