use crate::domain::{PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct ShotAttemptedPayload {
    #[serde(rename = "shooterId")]
    pub shooter_id: PlayerId,
    #[serde(rename = "teamId")]
    pub team_id: TeamId,
    #[serde(rename = "onTarget")]
    pub on_target: bool,
    pub outcome: String,
}
