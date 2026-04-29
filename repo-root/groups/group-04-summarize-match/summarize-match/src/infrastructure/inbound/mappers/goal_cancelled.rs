use crate::domain::{DomainEvent, GoalCancelled};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, GoalCancelledPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct GoalCancelledMapper;

impl EventMapper for GoalCancelledMapper {
    fn event_type(&self) -> &'static str {
        "GOAL_CANCELLED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: GoalCancelledPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::GoalCancelled(GoalCancelled {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            cancelled_goal_event_id: payload.cancelled_goal_event_id,
            reason: payload.reason,
        }))
    }
}
