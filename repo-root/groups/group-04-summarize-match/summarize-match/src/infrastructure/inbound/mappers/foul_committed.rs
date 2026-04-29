use crate::domain::{DomainEvent, FoulCommitted};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, FoulCommittedPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct FoulCommittedMapper;

impl EventMapper for FoulCommittedMapper {
    fn event_type(&self) -> &'static str {
        "FOUL_COMMITTED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: FoulCommittedPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::FoulCommitted(FoulCommitted {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            player_id: payload.player_id,
            team_id: payload.team_id,
            against_player_id: payload.against_player_id,
        }))
    }
}
