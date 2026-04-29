use crate::domain::{PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct FoulCommittedPayload {
    #[serde(rename = "playerId")]
    pub player_id: PlayerId,
    #[serde(rename = "teamId")]
    pub team_id: TeamId,
    #[serde(rename = "againstPlayerId")]
    pub against_player_id: Option<PlayerId>,
}
