use crate::domain::{EventId, PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct RedCardPayload {
    #[serde(rename = "playerId")]
    pub player_id: PlayerId,
    #[serde(rename = "teamId")]
    pub team_id: TeamId,
    #[serde(rename = "isDoubleYellow")]
    pub is_double_yellow: bool,
    #[serde(rename = "relatedFoulEventId")]
    pub related_foul_event_id: Option<EventId>,
}
