# BaseEvent

## Role

`BaseEvent` est l'enveloppe commune a tous les events de match consommes par `summarize-match`.

## Structure

```json
{
  "eventId": "uuid-v4",
  "matchId": "uuid-v4",
  "type": "MATCH_STARTED",
  "occurredAt": "2024-11-15T20:00:00.000Z",
  "matchTime": {
    "minute": 0,
    "second": 0,
    "period": "FIRST_HALF"
  },
  "payload": {}
}
```

## Champs communs

| Champ | Type | Obligatoire | Description |
|---|---|---:|---|
| `eventId` | `string (uuid-v4)` | ✅ | Identifiant unique de l'event |
| `matchId` | `string (uuid-v4)` | ✅ | Identifiant du match concerne |
| `type` | `string (enum)` | ✅ | Type d'event du catalogue |
| `occurredAt` | `string (ISO 8601 UTC)` | ✅ | Horodatage absolu de l'occurrence |
| `matchTime.minute` | `integer >= 0` | ✅ | Minute de jeu |
| `matchTime.second` | `integer [0-59]` | ✅ | Seconde de jeu |
| `matchTime.period` | `string (enum)` | ✅ | `FIRST_HALF` ou `SECOND_HALF` |
| `payload` | `object` | ✅ | Donnees specifiques au type d'event |

## Regles globales

- Les events doivent rester coherents avec la feuille de match du `matchId`.
- La timeline est ordonnee par `matchTime`, puis par `occurredAt`.
- Les references vers `playerId` et `teamId` doivent appartenir aux participants du match.
- Un event de jeu referenceant un joueur expulse apres `RED_CARD` est invalide.
- `tirs_cadres <= tirs` et `passes_reussies <= passes` sont des invariants de domaine verifies par le contexte `summarize-match`.

## Portee hexagonale

Cette enveloppe constitue le contrat du port entrant. Le consumer technique ne doit pas l'etendre; il doit seulement la valider, la traduire et la remettre au modele metier.
