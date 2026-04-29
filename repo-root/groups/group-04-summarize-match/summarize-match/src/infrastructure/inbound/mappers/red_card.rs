use crate::domain::{DomainEvent, RedCard};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, RedCardPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct RedCardMapper;

impl EventMapper for RedCardMapper {
    fn event_type(&self) -> &'static str {
        "RED_CARD"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: RedCardPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::RedCard(RedCard {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            player_id: payload.player_id,
            team_id: payload.team_id,
            is_double_yellow: payload.is_double_yellow,
            related_foul_event_id: payload.related_foul_event_id,
        }))
    }
}
