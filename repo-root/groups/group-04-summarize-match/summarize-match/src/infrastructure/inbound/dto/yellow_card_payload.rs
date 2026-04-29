use crate::domain::{EventId, PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct YellowCardPayload {
    #[serde(rename = "playerId")]
    pub player_id: PlayerId,
    #[serde(rename = "teamId")]
    pub team_id: TeamId,
    #[serde(rename = "relatedFoulEventId")]
    pub related_foul_event_id: Option<EventId>,
    #[serde(rename = "cardNumber")]
    pub card_number: u8,
}
