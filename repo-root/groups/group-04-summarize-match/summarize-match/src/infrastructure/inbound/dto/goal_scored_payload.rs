use crate::domain::{PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct GoalScoredPayload {
    #[serde(rename = "scoringTeamId")]
    pub scoring_team_id: TeamId,
    #[serde(rename = "scorerId")]
    pub scorer_id: PlayerId,
    #[serde(rename = "assistId")]
    pub assist_id: Option<PlayerId>,
    #[serde(rename = "isOwnGoal")]
    pub is_own_goal: bool,
}
