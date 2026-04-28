use crate::domain::{DomainEvent, GoalScored, MatchFinished, MatchStarted, Team, TeamId, PlayerId};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::BaseEvent;
use std::collections::HashMap;
use serde::Deserialize;

pub trait EventMapper: Send + Sync {
    fn event_type(&self) -> &'static str;
    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError>;
}

pub struct MatchStartedMapper;

#[derive(Debug, Deserialize)]
struct GoalScoredPayload {
    #[serde(rename = "scoringTeamId")]
    scoring_team_id: TeamId,
    #[serde(rename = "scorerId")]
    scorer_id: PlayerId,
    #[serde(rename = "assistId")]
    assist_id: Option<PlayerId>,
    #[serde(rename = "isOwnGoal")]
    is_own_goal: bool,
}

#[derive(Debug, Deserialize)]
struct MatchFinishedPayload {
    #[serde(rename = "finalScore")]
    final_score: FinalScore,
}

#[derive(Debug, Deserialize)]
struct FinalScore {
    home: u32,
    away: u32,
}

impl EventMapper for MatchStartedMapper {
    fn event_type(&self) -> &'static str {
        "MATCH_STARTED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let home_team: Team =
            serde_json::from_value(event.payload["homeTeam"].clone()).map_err(ValidationError::from)?;
        let away_team: Team =
            serde_json::from_value(event.payload["awayTeam"].clone()).map_err(ValidationError::from)?;
        let scheduled_duration = event.payload["scheduledDurationMinutes"]
            .as_u64()
            .ok_or_else(|| {
                ValidationError::Other("scheduledDurationMinutes missing or invalid".to_string())
            })? as u32;

        let match_started = MatchStarted {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            home_team,
            away_team,
            scheduled_duration_minutes: scheduled_duration,
        };

        Ok(DomainEvent::MatchStarted(match_started))
    }
}

pub struct GoalScoredMapper;

impl EventMapper for GoalScoredMapper {
    fn event_type(&self) -> &'static str {
        "GOAL_SCORED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: GoalScoredPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::GoalScored(GoalScored {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            scoring_team_id: payload.scoring_team_id,
            scorer_id: payload.scorer_id,
            assist_id: payload.assist_id,
            is_own_goal: payload.is_own_goal,
        }))
    }
}

pub struct MatchFinishedMapper;

impl EventMapper for MatchFinishedMapper {
    fn event_type(&self) -> &'static str {
        "MATCH_FINISHED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: MatchFinishedPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::MatchFinished(MatchFinished {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            final_score: crate::domain::Score {
                home: payload.final_score.home,
                away: payload.final_score.away,
            },
        }))
    }
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
        registry.register(Box::new(MatchFinishedMapper));
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
    fn unsupported_event_type_fails() {
        let registry = MapperRegistry::with_defaults();
        let mut event = valid_match_started_event();
        event.event_type = "UNKNOWN_EVENT".to_string();
        let mapped = registry.map(&event);
        assert!(mapped.is_err(), "unknown event should fail mapping");
    }
}
