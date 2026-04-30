# Diagrams

PlantUML diagrams for the `summarize-match` bounded context.

- [domain-class-diagram.puml](domain-class-diagram.puml) - domain model, value objects, events, read models and aggregate.
- [application-class-diagram.puml](application-class-diagram.puml) - application services and ports.
- [infrastructure-class-diagram.puml](infrastructure-class-diagram.puml) - inbound adapters, mappers, repositories and outbound adapters.
- [event-and-stats-sequence.puml](event-and-stats-sequence.puml) - sequence from `POST /events` to aggregate validation, persistence, stored stats update and stats queries.
- [ARCHITECTURE_DECISIONS.md](ARCHITECTURE_DECISIONS.md) - explanation of the diagrams and architectural choices regarding DDD, hexagonal architecture and SOLID.

The class diagram is split into three files to keep PlantUML output readable in viewers that crop wide diagrams.

Note: `events/outbound/` is intentionally empty today because the bounded context does not publish business outbound events. Persistence and logging adapters are infrastructure concerns, not outbound domain events.
