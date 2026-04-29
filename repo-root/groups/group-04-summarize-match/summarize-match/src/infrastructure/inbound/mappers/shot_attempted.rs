use crate::domain::{DomainEvent, ShotAttempted};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, ShotAttemptedPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct ShotAttemptedMapper;

impl EventMapper for ShotAttemptedMapper {
    fn event_type(&self) -> &'static str {
        "SHOT_ATTEMPTED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: ShotAttemptedPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::ShotAttempted(ShotAttempted {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            player_id: payload.shooter_id,
            team_id: payload.team_id,
            on_target: payload.on_target,
            outcome: payload.outcome,
        }))
    }
}
