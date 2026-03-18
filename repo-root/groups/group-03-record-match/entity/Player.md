# Player

**Résumé métier**

Représentation locale d'un joueur de five dans le contexte Record Match. Un Player n'est pas géré ici (c'est la responsabilité du groupe Manage Player), mais il est référencé dans les événements d'un match comme joueur principal ou joueur secondaire.

**Attributs**

- **id** - identifiant unique du joueur (UUID fourni par le groupe Manage Player)
- **name** - nom et prénom du joueur

**Invariants**

- **id** doit être un UUID valide et non vide
- **name** doit être une chaîne non vide

**Format JSON attendu**

- **Schéma** : `tests/schemas/player.schema.json`
- **Fixture valide** : `tests/fixtures/player.valid.json`
- **Fixture invalide** : `tests/fixtures/player.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec un id UUID valide, un firstName et un lastName non vides ne lève pas d'exception.
- **createInvalidIdThrows** - id non UUID lève une exception métier.
- **createInvalidEmptyNameThrows** - name vide lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.
