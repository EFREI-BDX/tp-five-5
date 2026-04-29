use crate::domain::{DomainEvent, MatchFinished};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, MatchFinishedPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

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
            final_score: payload.final_score,
        }))
    }
}
