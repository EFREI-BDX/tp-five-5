use crate::domain::{DomainEvent, MatchPaused};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, ReasonPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct MatchPausedMapper;

impl EventMapper for MatchPausedMapper {
    fn event_type(&self) -> &'static str {
        "MATCH_PAUSED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: ReasonPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::MatchPaused(MatchPaused {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            reason: payload.reason,
        }))
    }
}
