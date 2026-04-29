# Aggregates — index

Fiches par agregat pour le domaine `summarize-match`.

- [MatchAggregate](MatchAggregate.md) — racine d'agregat qui rejoue la timeline d'events et garantit les regles metier du match.

## Frontiere DDD

Un agregat est un objet du coeur domaine qui concentre les invariants. Les adapters HTTP, JSON Schema, SeaORM ou PostgreSQL ne doivent pas porter ces regles : ils alimentent ou persistent l'agregat via les ports applicatifs.

Dans ce bounded context, `MatchAggregate` est reconstruit depuis l'event store (`match_events`) au lieu d'etre stocke comme une ligne complete.
