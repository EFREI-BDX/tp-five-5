# Match

**Résumé métier**

Un match de five opposant deux équipes. Agrégat racine du bounded context Record Match. Contient la liste de tous les événements enregistrés pendant la partie.

**Attributs**

- **id** - identifiant unique du match
- **startTime** - heure de début du match
- **teamHomeId** - référence à l'équipe qui reçoit
- **teamAwayId** - référence à l'équipe visiteuse
- **events** - liste ordonnée des événements survenus pendant le match

**Invariants**

- **id** doit être un UUID valide et non vide
- **startTime** doit être un datetime ISO-8601 valide
- **teamHomeId** doit être un UUID valide et différent de **teamAwayId**
- **teamAwayId** doit être un UUID valide et différent de **teamHomeId**
- **events** doit être trié par ordre croissant de timestamp

**Format JSON attendu**

- **Schéma** : `tests/schemas/match.schema.json`
- **Fixture valide** : `tests/fixtures/match.valid.json`
- **Fixture invalide** : `tests/fixtures/match.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec des valeurs valides ne lève pas d'exception.
- **createInvalidSameTeams** - teamHomeId identique à teamAwayId lève une exception métier.
- **addEvent** - l'ajout d'un événement valide est ajouté à la liste events.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.