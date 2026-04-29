use crate::domain::{PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct SubstitutionPayload {
    #[serde(rename = "playerOutId")]
    pub player_out: PlayerId,
    #[serde(rename = "playerInId")]
    pub player_in: PlayerId,
    #[serde(rename = "teamId")]
    pub team_id: TeamId,
}
