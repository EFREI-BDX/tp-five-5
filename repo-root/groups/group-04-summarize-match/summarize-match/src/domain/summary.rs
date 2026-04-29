use super::{MatchTime, PlayerId, Score, TeamId};
use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub enum MatchResult {
    Win,
    Loss,
    Draw,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum MatchStatus {
    NotStarted,
    InProgress,
    Paused,
    Finished,
    Cancelled,
    Forfeited,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(tag = "type", rename_all = "SCREAMING_SNAKE_CASE")]
pub enum CardType {
    Yellow { card_number: u8 },
    Red { is_double_yellow: bool },
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct GoalEntry {
    pub event_id: String,
    pub scoring_team_id: TeamId,
    pub scorer_id: PlayerId,
    pub assist_id: Option<PlayerId>,
    pub is_own_goal: bool,
    pub match_time: MatchTime,
    pub cancelled: bool,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct CardEntry {
    pub event_id: String,
    pub player_id: PlayerId,
    pub team_id: TeamId,
    pub match_time: MatchTime,
    #[serde(flatten)]
    pub card_type: CardType,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SubstitutionEntry {
    pub event_id: String,
    pub team_id: TeamId,
    pub player_out: PlayerId,
    pub player_in: PlayerId,
    pub match_time: MatchTime,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct MatchSummary {
    pub match_id: String,
    pub status: MatchStatus,
    pub home_team_id: Option<TeamId>,
    pub away_team_id: Option<TeamId>,
    pub score: Score,
    pub goals: Vec<GoalEntry>,
    pub cards: Vec<CardEntry>,
    pub substitutions: Vec<SubstitutionEntry>,
}
