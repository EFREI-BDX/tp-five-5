use crate::domain::Team;
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct MatchStartedPayload {
    #[serde(rename = "homeTeam")]
    pub home_team: Team,
    #[serde(rename = "awayTeam")]
    pub away_team: Team,
    #[serde(rename = "scheduledDurationMinutes")]
    pub scheduled_duration_minutes: u32,
}
