use crate::domain::{DomainEvent, YellowCard};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, YellowCardPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct YellowCardMapper;

impl EventMapper for YellowCardMapper {
    fn event_type(&self) -> &'static str {
        "YELLOW_CARD"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: YellowCardPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::YellowCard(YellowCard {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            player_id: payload.player_id,
            team_id: payload.team_id,
            related_foul_event_id: payload.related_foul_event_id,
            card_number: payload.card_number,
        }))
    }
}
