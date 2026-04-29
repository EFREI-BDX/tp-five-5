# StartMatch 

**Resume metier**

Evenement recu au debut d'un match. Il permet d'enregistrer le match et les deux equipes engagees.

**Declencheur**

- Une commande de demarrage de match est validee.

**Payload JSON**

- `matchId` - identifiant du match
- `team1Id` - identifiant de la premiere equipe
- `team2Id` - identifiant de la deuxieme equipe
- `startedAt` - date et heure de debut du match

**Invariants**

- `matchId` doit etre non vide
- `matchId` doit etre un UUID valide
- `team1Id` doit etre non vide
- `team1Id` doit etre un UUID valide
- `team2Id` doit etre non vide
- `team2Id` doit etre un UUID valide
- `team1Id` et `team2Id` doivent etre differents
- `startedAt` doit etre un datetime ISO-8601 valide

**Format JSON attendu**

```json
{
  "matchId": "550e8400-e29b-41d4-a716-446655440000",
  "team1Id": "550e8400-e29b-41d4-a716-446655440001",
  "team2Id": "550e8400-e29b-41d4-a716-446655440002",
  "startedAt": "2024-11-15T20:00:00.000Z"
}
```

**Producteur**

- Service metier de gestion de match

**Consommateur**

- Service metier Record Match

**Tests minimaux attendus**

- verifier qu'un payload avec `matchId`, `team1Id`, `team2Id` et `startedAt` valides est accepte
- verifier qu'un payload avec un `matchId` invalide est rejete
- verifier qu'un payload avec `team1Id` egal a `team2Id` est rejete
- verifier qu'un payload avec un `startedAt` invalide est rejete

**Remarques**

Cet evenement entrant represente le fait metier "un match a commence".