use crate::domain::{DomainEvent, Substitution};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, SubstitutionPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct SubstitutionMapper;

impl EventMapper for SubstitutionMapper {
    fn event_type(&self) -> &'static str {
        "SUBSTITUTION"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: SubstitutionPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::Substitution(Substitution {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            player_out: payload.player_out,
            player_in: payload.player_in,
            team_id: payload.team_id,
        }))
    }
}
