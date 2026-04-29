use crate::domain::{DomainEvent, PassAttempted};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, PassAttemptedPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct PassAttemptedMapper;

impl EventMapper for PassAttemptedMapper {
    fn event_type(&self) -> &'static str {
        "PASS_ATTEMPTED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: PassAttemptedPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::PassAttempted(PassAttempted {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            player_id: payload.passer_id,
            team_id: payload.team_id,
            target_player_id: payload.receiver_id,
            succeeded: payload.succeeded,
        }))
    }
}
