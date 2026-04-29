use crate::domain::{DomainEvent, GoalScored};
use crate::infrastructure::error::ValidationError;
use crate::infrastructure::inbound::dto::{BaseEvent, GoalScoredPayload};
use crate::infrastructure::inbound::mapper_registry::EventMapper;

pub struct GoalScoredMapper;

impl EventMapper for GoalScoredMapper {
    fn event_type(&self) -> &'static str {
        "GOAL_SCORED"
    }

    fn map(&self, event: &BaseEvent) -> Result<DomainEvent, ValidationError> {
        let payload: GoalScoredPayload =
            serde_json::from_value(event.payload.clone()).map_err(ValidationError::from)?;

        Ok(DomainEvent::GoalScored(GoalScored {
            event_id: event.event_id.clone(),
            match_id: event.match_id.clone(),
            occurred_at: event.occurred_at.clone(),
            match_time: event.match_time.clone(),
            scoring_team_id: payload.scoring_team_id,
            scorer_id: payload.scorer_id,
            assist_id: payload.assist_id,
            is_own_goal: payload.is_own_goal,
        }))
    }
}
