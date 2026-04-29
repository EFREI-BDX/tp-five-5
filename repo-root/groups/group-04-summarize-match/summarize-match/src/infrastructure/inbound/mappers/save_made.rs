use crate::domain::{DomainEvent, SaveMade};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, SaveMadePayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct SaveMadeMapper;

impl EventMapper for SaveMadeMapper {
    fn event_type(&self) -> &'static str {
        "SAVE_MADE"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: SaveMadePayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::SaveMade(SaveMade {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            keeper_id: payload.keeper_id,
            keeper_team_id: payload.keeper_team_id,
            related_shot_event_id: payload.related_shot_event_id,
        }))
    }
}
