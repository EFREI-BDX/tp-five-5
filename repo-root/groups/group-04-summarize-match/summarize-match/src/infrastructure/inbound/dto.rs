use crate::domain::MatchTime;
use serde::{Deserialize, Serialize};
use serde_json::Value;

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct BaseEvent {
    #[serde(rename = "eventId")]
    pub event_id: String,

    #[serde(rename = "matchId")]
    pub match_id: String,

    #[serde(rename = "type")]
    pub event_type: String,

    #[serde(rename = "occurredAt")]
    pub occurred_at: String,

    #[serde(rename = "matchTime")]
    pub match_time: MatchTime,

    pub payload: Value,
}
