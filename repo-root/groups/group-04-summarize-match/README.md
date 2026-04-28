# summarize-match

Service de resume de match dans une architecture DDD hexagonale.

## Documentation canonique

- `value-object/*.md` - fiches par Value Object (PlayerId, TeamId)

## Ce que fait le service

- Consomme un flux d'events de match venant du contexte amont `record-match`.
- Reconstitue une timeline coherente.
- Calcule les scores et les statistiques derives.
- Signale les anomalies metier quand la timeline ou le score final ne sont pas coherents.

## Ce que le service ne fait pas encore

- Pas de port HTTP metier expose a part le healthcheck.
- Pas d'adapters persistants documentes dans ce repo.
- Pas de schemas JSON par event encore formalisés dans `tests/schemas` pour toute la matrice du contrat.
