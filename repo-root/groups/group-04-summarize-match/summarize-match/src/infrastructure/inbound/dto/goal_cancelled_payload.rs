use crate::domain::EventId;
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct GoalCancelledPayload {
    #[serde(rename = "cancelledGoalEventId")]
    pub cancelled_goal_event_id: EventId,
    pub reason: String,
}
