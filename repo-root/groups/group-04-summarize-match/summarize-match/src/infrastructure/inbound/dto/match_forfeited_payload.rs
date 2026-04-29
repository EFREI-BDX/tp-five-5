use crate::domain::{Score, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct MatchForfeitedPayload {
    #[serde(rename = "forfeitingTeamId")]
    pub forfeiting_team_id: TeamId,
    pub reason: String,
    #[serde(rename = "administrativeScore")]
    pub administrative_score: Score,
    #[serde(rename = "statsPolicy")]
    pub stats_policy: String,
}
