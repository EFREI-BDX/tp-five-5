use crate::domain::DomainEvent;
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::BaseEvent;
use crate::infrastructure::inbound::mappers::{
    FoulCommittedMapper, GoalCancelledMapper, GoalScoredMapper, MatchCancelledMapper,
    MatchFinishedMapper, MatchForfeitedMapper, MatchPausedMapper, MatchResumedMapper,
    MatchStartedMapper, PassAttemptedMapper, RedCardMapper, SaveMadeMapper, ShotAttemptedMapper,
    SubstitutionMapper, YellowCardMapper,
};
use std::collections::HashMap;

pub trait EventMapper: Send + Sync {
    fn event_type(&self) -> &'static str;
    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError>;
}

pub struct MapperRegistry {
    mappers: HashMap<&'static str, Box<dyn EventMapper>>,
}

impl MapperRegistry {
    pub fn with_defaults() -> Self {
        let mut registry = Self {
            mappers: HashMap::new(),
        };
        registry.register(Box::new(MatchStartedMapper));
        registry.register(Box::new(GoalScoredMapper));
        registry.register(Box::new(GoalCancelledMapper));
        registry.register(Box::new(MatchFinishedMapper));
        registry.register(Box::new(RedCardMapper));
        registry.register(Box::new(PassAttemptedMapper));
        registry.register(Box::new(ShotAttemptedMapper));
        registry.register(Box::new(FoulCommittedMapper));
        registry.register(Box::new(YellowCardMapper));
        registry.register(Box::new(SaveMadeMapper));
        registry.register(Box::new(SubstitutionMapper));
        registry.register(Box::new(MatchPausedMapper));
        registry.register(Box::new(MatchResumedMapper));
        registry.register(Box::new(MatchCancelledMapper));
        registry.register(Box::new(MatchForfeitedMapper));
        registry
    }

    pub fn register(&mut self, mapper: Box<dyn EventMapper>) {
        self.mappers.insert(mapper.event_type(), mapper);
    }

    pub fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let mapper = self.mappers.get(event.event_type.as_str()).ok_or_else(|| {
            ValidationError::Other(format!("unsupported event type: {}", event.event_type))
        })?;

        mapper.map(event)
    }
}

#[cfg(test)]
mod tests {
    use super::MapperRegistry;
    use crate::domain::MatchTime;
    use crate::infrastructure::inbound::dto::BaseEvent;
    use serde_json::json;

    fn valid_match_started_event() -> BaseEvent {
        BaseEvent {
            event_id: "550e8400-e29b-41d4-a716-446655440000".to_string(),
            match_id: "11111111-2222-3333-4444-555555555555".to_string(),
            event_type: "MATCH_STARTED".to_string(),
            occurred_at: "2026-04-28T12:00:00Z".to_string(),
            match_time: MatchTime {
                minute: 0,
                second: 0,
                period: "FIRST_HALF".to_string(),
            },
            payload: json!({
                "homeTeam": {
                    "teamId": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
                    "startingPlayers": [
                        { "playerId": "00000000-0000-0000-0000-000000000001", "isGoalkeeper": true },
                        { "playerId": "00000000-0000-0000-0000-000000000002", "isGoalkeeper": false },
                        { "playerId": "00000000-0000-0000-0000-000000000003", "isGoalkeeper": false },
                        { "playerId": "00000000-0000-0000-0000-000000000004", "isGoalkeeper": false },
                        { "playerId": "00000000-0000-0000-0000-000000000005", "isGoalkeeper": false }
                    ]
                },
                "awayTeam": {
                    "teamId": "ffffffff-eeee-dddd-cccc-bbbbbbbbbbbb",
                    "startingPlayers": [
                        { "playerId": "00000000-0000-0000-0000-000000000006", "isGoalkeeper": true },
                        { "playerId": "00000000-0000-0000-0000-000000000007", "isGoalkeeper": false },
                        { "playerId": "00000000-0000-0000-0000-000000000008", "isGoalkeeper": false },
                        { "playerId": "00000000-0000-0000-0000-000000000009", "isGoalkeeper": false },
                        { "playerId": "00000000-0000-0000-0000-000000000010", "isGoalkeeper": false }
                    ]
                },
                "scheduledDurationMinutes": 40
            }),
        }
    }

    #[test]
    fn map_match_started_success() {
        let registry = MapperRegistry::with_defaults();
        let event = valid_match_started_event();
        let mapped = registry.map(&event);
        assert!(mapped.is_ok(), "MATCH_STARTED should be mapped");
    }

    #[test]
    fn map_red_card_success() {
        let registry = MapperRegistry::with_defaults();
        let mut event = valid_match_started_event();
        event.event_type = "RED_CARD".to_string();
        event.payload = serde_json::json!({
            "playerId": "00000000-0000-0000-0000-000000000002",
            "teamId": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
            "isDoubleYellow": false,
            "relatedFoulEventId": null
        });

        let mapped = registry.map(&event);
        if let Err(e) = &mapped {
            panic!("RED_CARD mapping failed: {:?}", e);
        }
        assert!(mapped.is_ok(), "RED_CARD should be mapped");
    }

    #[test]
    fn unsupported_event_type_fails() {
        let registry = MapperRegistry::with_defaults();
        let mut event = valid_match_started_event();
        event.event_type = "UNKNOWN_EVENT".to_string();
        let mapped = registry.map(&event);
        assert!(mapped.is_err(), "unknown event should fail mapping");
    }
}
