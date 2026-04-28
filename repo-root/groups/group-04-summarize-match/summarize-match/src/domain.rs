use serde::{Deserialize, Serialize};
use serde::de::{self, Deserializer};
use uuid::Uuid;

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchTime {
    pub minute: u32,
    pub second: u32,
    pub period: String,
}

#[derive(Debug, Clone, Serialize, PartialEq, Eq, Hash)]
#[serde(transparent)]
pub struct PlayerId(pub String);

impl From<String> for PlayerId {
    fn from(s: String) -> Self {
        PlayerId(s)
    }
}

impl From<&str> for PlayerId {
    fn from(s: &str) -> Self {
        PlayerId(s.to_string())
    }
}


impl<'de> Deserialize<'de> for PlayerId {
    fn deserialize<D>(deserializer: D) -> Result<Self, D::Error>
    where
        D: Deserializer<'de>,
    {
        let s = String::deserialize(deserializer)?;
        Uuid::parse_str(&s).map_err(|e| de::Error::custom(format!("invalid PlayerId UUID: {}", e)))?;
        Ok(PlayerId(s))
    }
}

#[derive(Debug, Clone, Serialize, PartialEq, Eq, Hash)]
#[serde(transparent)]
pub struct TeamId(pub String);

impl From<String> for TeamId {
    fn from(s: String) -> Self {
        TeamId(s)
    }
}

impl From<&str> for TeamId {
    fn from(s: &str) -> Self {
        TeamId(s.to_string())
    }
}


impl<'de> Deserialize<'de> for TeamId {
    fn deserialize<D>(deserializer: D) -> Result<Self, D::Error>
    where
        D: Deserializer<'de>,
    {
        let s = String::deserialize(deserializer)?;
        Uuid::parse_str(&s).map_err(|e| de::Error::custom(format!("invalid TeamId UUID: {}", e)))?;
        Ok(TeamId(s))
    }
}

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
pub struct Score {
    pub home: u32,
    pub away: u32,
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct MatchFinished {
    pub event_id: String,
    pub match_id: String,
    pub occurred_at: String,
    pub match_time: MatchTime,
    pub final_score: Score,
}

#[derive(Debug, Clone, PartialEq)]
pub enum DomainEvent {
    MatchStarted(MatchStarted),
    GoalScored(GoalScored),
    MatchFinished(MatchFinished),
}

#[cfg(test)]
mod tests {
    use super::*;
    use serde_json;

    #[test]
    fn playerid_deserialize_valid_uuid() {
        let json = "\"00000000-0000-0000-0000-000000000001\"";
        let pid: PlayerId = serde_json::from_str(json).expect("should parse uuid");
        assert_eq!(pid.0, "00000000-0000-0000-0000-000000000001");
    }

    #[test]
    fn playerid_deserialize_invalid_uuid() {
        let json = "\"not-a-uuid\"";
        let res: Result<PlayerId, _> = serde_json::from_str(json);
        assert!(res.is_err(), "invalid uuid should fail deserialization");
    }

    #[test]
    fn teamid_deserialize_valid_uuid() {
        let json = "\"aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee\"";
        let tid: TeamId = serde_json::from_str(json).expect("should parse uuid");
        assert_eq!(tid.0, "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");
    }

    #[test]
    fn teamid_deserialize_invalid_uuid() {
        let json = "\"12345\"";
        let res: Result<TeamId, _> = serde_json::from_str(json);
        assert!(res.is_err(), "invalid uuid should fail deserialization");
    }
}
