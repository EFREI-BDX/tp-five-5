use crate::domain::{PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct PassAttemptedPayload {
    #[serde(rename = "passerId")]
    pub passer_id: PlayerId,
    #[serde(rename = "teamId")]
    pub team_id: TeamId,
    #[serde(rename = "receiverId")]
    pub receiver_id: Option<PlayerId>,
    pub succeeded: bool,
}
