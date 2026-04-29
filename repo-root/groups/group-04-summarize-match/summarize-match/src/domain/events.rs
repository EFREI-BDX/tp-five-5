use super::value_objects::{EventId, MatchTime, PlayerId, Score, TeamId};
use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct Player {
    #[serde(rename = "playerId")]
    pub player_id: PlayerId,
    #[serde(rename = "isGoalkeeper")]
    pub is_goalkeeper: bool,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct Team {
    #[serde(rename = "teamId")]
    pub team_id: TeamId,
    #[serde(rename = "startingPlayers")]
    pub starting_players: Vec<Player>,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchStarted {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub home_team: Team,
    pub away_team: Team,
    #[serde(rename = "scheduledDurationMinutes")]
    pub scheduled_duration_minutes: u32,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct GoalScored {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub scoring_team_id: TeamId,
    pub scorer_id: PlayerId,
    pub assist_id: Option<PlayerId>,
    pub is_own_goal: bool,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct GoalCancelled {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub cancelled_goal_event_id: EventId,
    pub reason: String,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchFinished {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub final_score: Score,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct RedCard {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub player_id: PlayerId,
    pub team_id: TeamId,
    pub is_double_yellow: bool,
    pub related_foul_event_id: Option<EventId>,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct PassAttempted {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub player_id: PlayerId,
    pub team_id: TeamId,
    pub target_player_id: Option<PlayerId>,
    pub succeeded: bool,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct ShotAttempted {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub player_id: PlayerId,
    pub team_id: TeamId,
    pub on_target: bool,
    pub outcome: String,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct FoulCommitted {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub player_id: PlayerId,
    pub team_id: TeamId,
    pub against_player_id: Option<PlayerId>,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct YellowCard {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub player_id: PlayerId,
    pub team_id: TeamId,
    pub related_foul_event_id: Option<EventId>,
    pub card_number: u8,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct SaveMade {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub keeper_id: PlayerId,
    pub keeper_team_id: TeamId,
    pub related_shot_event_id: Option<EventId>,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct Substitution {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub player_out: PlayerId,
    pub player_in: PlayerId,
    pub team_id: TeamId,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchPaused {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub reason: String,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchResumed {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub reason: String,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchCancelled {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub reason: String,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchForfeited {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub forfeiting_team_id: TeamId,
    pub reason: String,
    pub administrative_score: Score,
    pub stats_policy: String,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub enum DomainEvent {
    MatchStarted(MatchStarted),
    GoalScored(GoalScored),
    GoalCancelled(GoalCancelled),
    MatchFinished(MatchFinished),
    RedCard(RedCard),
    PassAttempted(PassAttempted),
    ShotAttempted(ShotAttempted),
    FoulCommitted(FoulCommitted),
    YellowCard(YellowCard),
    SaveMade(SaveMade),
    Substitution(Substitution),
    MatchPaused(MatchPaused),
    MatchResumed(MatchResumed),
    MatchCancelled(MatchCancelled),
    MatchForfeited(MatchForfeited),
}

impl DomainEvent {
    pub fn event_type(&self) -> &'static str {
        match self {
            DomainEvent::MatchStarted(_) => "MATCH_STARTED",
            DomainEvent::GoalScored(_) => "GOAL_SCORED",
            DomainEvent::GoalCancelled(_) => "GOAL_CANCELLED",
            DomainEvent::MatchFinished(_) => "MATCH_FINISHED",
            DomainEvent::RedCard(_) => "RED_CARD",
            DomainEvent::PassAttempted(_) => "PASS_ATTEMPTED",
            DomainEvent::ShotAttempted(_) => "SHOT_ATTEMPTED",
            DomainEvent::FoulCommitted(_) => "FOUL_COMMITTED",
            DomainEvent::YellowCard(_) => "YELLOW_CARD",
            DomainEvent::SaveMade(_) => "SAVE_MADE",
            DomainEvent::Substitution(_) => "SUBSTITUTION",
            DomainEvent::MatchPaused(_) => "MATCH_PAUSED",
            DomainEvent::MatchResumed(_) => "MATCH_RESUMED",
            DomainEvent::MatchCancelled(_) => "MATCH_CANCELLED",
            DomainEvent::MatchForfeited(_) => "MATCH_FORFEITED",
        }
    }

    pub fn match_id(&self) -> &str {
        match self {
            DomainEvent::MatchStarted(event) => &event.match_id,
            DomainEvent::GoalScored(event) => &event.match_id,
            DomainEvent::GoalCancelled(event) => &event.match_id,
            DomainEvent::MatchFinished(event) => &event.match_id,
            DomainEvent::RedCard(event) => &event.match_id,
            DomainEvent::PassAttempted(event) => &event.match_id,
            DomainEvent::ShotAttempted(event) => &event.match_id,
            DomainEvent::FoulCommitted(event) => &event.match_id,
            DomainEvent::YellowCard(event) => &event.match_id,
            DomainEvent::SaveMade(event) => &event.match_id,
            DomainEvent::Substitution(event) => &event.match_id,
            DomainEvent::MatchPaused(event) => &event.match_id,
            DomainEvent::MatchResumed(event) => &event.match_id,
            DomainEvent::MatchCancelled(event) => &event.match_id,
            DomainEvent::MatchForfeited(event) => &event.match_id,
        }
    }
}
