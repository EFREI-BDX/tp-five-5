use crate::domain::{DomainEvent, MatchStarted};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, MatchStartedPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct MatchStartedMapper;

impl EventMapper for MatchStartedMapper {
    fn event_type(&self) -> &'static str {
        "MATCH_STARTED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: MatchStartedPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::MatchStarted(MatchStarted {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            home_team: payload.home_team,
            away_team: payload.away_team,
            scheduled_duration_minutes: payload.scheduled_duration_minutes,
        }))
    }
}
