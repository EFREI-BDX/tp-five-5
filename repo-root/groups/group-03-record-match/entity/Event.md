# Event

**Résumé métier**

Un événement survenu pendant un match de five (but, faute, carton, etc.). Un événement est immuable une fois enregistré.

**Attributs**

- **id** - identifiant unique de l'événement
- **matchId** - référence au match auquel appartient cet événement
- **timestamp** - horodatage de l'événement au sein du match
- **action** - type d'action (but, faute, carton jaune, carton rouge, coup d'envoi, fin de match)
- **primaryPlayerId** *(optionnel)* - référence au joueur principal de l'action
- **secondaryPlayerId** *(optionnel)* - référence au joueur secondaire de l'action

**Règles de présence des joueurs par action**

| Action       | primaryPlayerId | secondaryPlayerId |
|--------------|-----------------|-------------------|
| `kickOff`    | absent          | absent            |
| `endMatch`   | absent          | absent            |
| `goal`       | obligatoire (buteur) | absent       |
| `foul`       | obligatoire (fautif) | obligatoire (victime) |
| `yellowCard` | obligatoire (joueur averti) | absent     |
| `redCard`    | obligatoire (joueur expulsé) | absent    |
| `assist`     |obligatoire (passeur) | absent |
| `change`     | obligatoire (sortant) | obligatoire (entrant) |

**Invariants**

- **id** doit être un UUID valide et non vide
- **matchId** doit être un UUID valide et non vide
- **timestamp** doit être un datetime ISO-8601 valide
- **timestamp** doit être supérieur ou égal au startTime du match associé
- **action** doit appartenir à l'énumération autorisée
- **primaryPlayerId** si renseigné doit être un UUID valide
- **secondaryPlayerId** ne peut être renseigné que si **primaryPlayerId** l'est aussi
- **secondaryPlayerId** si renseigné doit être un UUID valide et différent de **primaryPlayerId**
- Si **action** est `foul` ou `change`, **primaryPlayerId** et **secondaryPlayerId** doivent tous deux être renseignés
- Si **action** est `assist`, `goal`, `yellowCard` ou `redCard`, **primaryPlayerId** doit être renseigné
- Si **action** est `kickOff` ou `endMatch`, **primaryPlayerId** et **secondaryPlayerId** doivent être absents

**Format JSON attendu**

- **Schéma** : `tests/schemas/event.schema.json`
- **Fixture valide** : `tests/fixtures/event.valid.json`
- **Fixture invalide** : `tests/fixtures/event.invalid.json`

**Tests minimaux attendus**

- **createValid** - construction avec des valeurs valides ne lève pas d'exception.
- **createInvalidActionThrows** - action hors énumération lève une exception métier.
- **createSecondaryWithoutPrimaryThrows** - secondaryPlayerId renseigné sans primaryPlayerId lève une exception métier.
- **createFoulOrChangeWithoutSecondaryThrows** - action `foul` ou `change` sans secondaryPlayerId lève une exception métier.
- **createKickOffWithPlayerThrows** - action `kickOff` ou `endMatch` avec un joueur renseigné lève une exception métier.
- **createInvalidSamePlayers** - primaryPlayerId identique à secondaryPlayerId lève une exception métier.
- **jsonRoundtrip** - sérialisation/désérialisation conserve toutes les valeurs.
- **schemaValidation** - fixture valide passe le schema ; fixture invalide échoue.
