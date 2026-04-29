# EndMatch 

**Resume metier**

Evenement recu a la fin d'un match. Il permet de cloturer le match et de figer l'heure de fin.

**Declencheur**

- Une commande de fin de match est validee.

**Payload JSON**

- `matchId` - identifiant du match
- `endedAt` - date et heure de fin du match

**Invariants**

- `matchId` doit etre non vide
- `matchId` doit etre un UUID valide
- `endedAt` doit etre un datetime ISO-8601 valide

**Format JSON attendu**

```json
{
  "matchId": "550e8400-e29b-41d4-a716-446655440000",
  "endedAt": "2024-11-15T21:35:00.000Z"
}
```

**Producteur**

- Service metier de gestion de match

**Consommateur**

- Service metier Record Match

**Tests minimaux attendus**

- verifier qu'un payload avec `matchId` et `endedAt` valides est accepte
- verifier qu'un payload avec un `matchId` invalide est rejete
- verifier qu'un payload avec un `endedAt` invalide est rejete

**Remarques**

Cet evenement entrant represente le fait metier "un match est termine".