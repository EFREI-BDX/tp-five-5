use crate::domain::{DomainEvent, MatchResumed};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, ReasonPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct MatchResumedMapper;

impl EventMapper for MatchResumedMapper {
    fn event_type(&self) -> &'static str {
        "MATCH_RESUMED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: ReasonPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::MatchResumed(MatchResumed {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            reason: payload.reason,
        }))
    }
}
