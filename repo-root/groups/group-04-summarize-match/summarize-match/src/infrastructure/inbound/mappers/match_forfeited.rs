use crate::domain::{DomainEvent, MatchForfeited};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, MatchForfeitedPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct MatchForfeitedMapper;

impl EventMapper for MatchForfeitedMapper {
    fn event_type(&self) -> &'static str {
        "MATCH_FORFEITED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: MatchForfeitedPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::MatchForfeited(MatchForfeited {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            forfeiting_team_id: payload.forfeiting_team_id,
            reason: payload.reason,
            administrative_score: payload.administrative_score,
            stats_policy: payload.stats_policy,
        }))
    }
}
