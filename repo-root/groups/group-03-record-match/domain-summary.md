# Domain Summary — Record Match

**Objectif métier**

Enregistrer les événements qui surviennent pendant un match de five : début de match, buts, fautes, cartons, fins de match. Ce contexte est la source de vérité pour le déroulement d'un match.

**Ubiquitous language**

- **Match** : une partie de five opposant deux équipes, identifiée par un id et une heure de début.
- **Event** : un événement survenu pendant un match (but, faute, carton jaune, carton rouge, coup d'envoi, fin de match). Un événement est lié à un match, porte un horodatage, un type d'action, et un joueur principal. Un joueur secondaire est optionnel (utile pour les fautes ou encore pour les changements).
- **Action** : type d'événement enregistré, appartenant à une énumération fixe.
- **StartTime** : heure de début d'un match, au format ISO-8601.
- **Timestamp** : horodatage d'un événement au sein d'un match, au format ISO-8601.
- **Id** : identifiant unique (UUID) d'un Match, d'un Event, d'un Player ou d'une Team.

**Principales invariants métier**

- **Match.id** doit être un UUID valide.
- **Match.startTime** doit être un datetime ISO-8601 valide.
- **Event.id** doit être un UUID valide.
- **Event.timestamp** doit être un datetime ISO-8601 valide, supérieur ou égal au startTime du match associé.
- **Event.action** doit appartenir à l'énumération autorisée (`goal`, `foul`, `yellowCard`, `redCard`, `kickOff`, `endMatch`).
- **Event.primaryPlayerId** doit référencer un joueur existant (géré par le groupe Manage Player).
- **Event.secondaryPlayerId** est optionnel, mais s'il est renseigné il doit référencer un joueur existant.

**Principaux events produits**

- **MatchStarted** — payload minimal : `matchId`, `startTime`, `teamHomeId`, `teamAwayId`, `timestamp`.
- **EventRecorded** — payload minimal : `eventId`, `matchId`, `timestamp`, `action`, `actorPlayerId`, `victimPlayerId` (optionnel).
- **MatchEnded** — payload minimal : `matchId`, `endTime`, `timestamp`.
