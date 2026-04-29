use serde::de::{self, Deserializer};
use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Deserialize)]
struct MatchTimeRaw {
    pub minute: u32,
    pub second: u32,
    pub period: String,
}

impl TryFrom<MatchTimeRaw> for MatchTime {
    type Error = String;

    fn try_from(raw: MatchTimeRaw) -> Result<Self, Self::Error> {
        if raw.second > 59 {
            return Err(format!(
                "MatchTime.second must be 0-59, got {}",
                raw.second
            ));
        }
        match raw.period.as_str() {
            "FIRST_HALF" | "SECOND_HALF" => {}
            other => {
                return Err(format!(
                    "MatchTime.period must be FIRST_HALF or SECOND_HALF, got {}",
                    other
                ))
            }
        }
        Ok(MatchTime {
            minute: raw.minute,
            second: raw.second,
            period: raw.period,
        })
    }
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
#[serde(try_from = "MatchTimeRaw")]
pub struct MatchTime {
    pub minute: u32,
    pub second: u32,
    pub period: String,
}

impl MatchTime {
    pub fn new(minute: u32, second: u32, period: String) -> Result<Self, String> {
        MatchTimeRaw {
            minute,
            second,
            period,
        }
        .try_into()
    }
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
        Uuid::parse_str(&s)
            .map_err(|e| de::Error::custom(format!("invalid PlayerId UUID: {}", e)))?;
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
        Uuid::parse_str(&s)
            .map_err(|e| de::Error::custom(format!("invalid TeamId UUID: {}", e)))?;
        Ok(TeamId(s))
    }
}

#[derive(Debug, Clone, Serialize, PartialEq, Eq, Hash)]
#[serde(transparent)]
pub struct EventId(pub String);

impl From<String> for EventId {
    fn from(s: String) -> Self {
        EventId(s)
    }
}

impl From<&str> for EventId {
    fn from(s: &str) -> Self {
        EventId(s.to_string())
    }
}

impl<'de> Deserialize<'de> for EventId {
    fn deserialize<D>(deserializer: D) -> Result<Self, D::Error>
    where
        D: Deserializer<'de>,
    {
        let s = String::deserialize(deserializer)?;
        Uuid::parse_str(&s)
            .map_err(|e| de::Error::custom(format!("invalid EventId UUID: {}", e)))?;
        Ok(EventId(s))
    }
}

#[derive(Debug, Clone, Serialize, PartialEq, Eq, Hash)]
#[serde(transparent)]
pub struct MatchId(pub String);

impl MatchId {
    pub fn new(s: impl Into<String>) -> Result<Self, String> {
        let s = s.into();
        Uuid::parse_str(&s)
            .map_err(|e| format!("invalid MatchId UUID: {}", e))?;
        Ok(MatchId(s))
    }

    pub fn as_str(&self) -> &str {
        &self.0
    }
}

impl<'de> Deserialize<'de> for MatchId {
    fn deserialize<D>(deserializer: D) -> Result<Self, D::Error>
    where
        D: Deserializer<'de>,
    {
        let s = String::deserialize(deserializer)?;
        Uuid::parse_str(&s)
            .map_err(|e| de::Error::custom(format!("invalid MatchId UUID: {}", e)))?;
        Ok(MatchId(s))
    }
}

#[derive(Debug, Clone, Serialize, Deserialize, PartialEq)]
pub struct Score {
    pub home: u32,
    pub away: u32,
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

    #[test]
    fn matchid_new_rejects_invalid_uuid() {
        assert!(MatchId::new("not-a-uuid").is_err());
    }

    #[test]
    fn matchid_new_accepts_valid_uuid() {
        let id = MatchId::new("11111111-2222-3333-4444-555555555555").unwrap();
        assert_eq!(id.as_str(), "11111111-2222-3333-4444-555555555555");
    }

    #[test]
    fn match_time_rejects_invalid_period() {
        let result = MatchTime::new(10, 30, "EXTRA_TIME".to_string());
        assert!(result.is_err(), "invalid period should be rejected");
    }

    #[test]
    fn match_time_rejects_second_over_59() {
        let result = MatchTime::new(10, 60, "FIRST_HALF".to_string());
        assert!(result.is_err(), "second >= 60 should be rejected");
    }

    #[test]
    fn match_time_accepts_valid_values() {
        let t = MatchTime::new(45, 0, "SECOND_HALF".to_string()).unwrap();
        assert_eq!(t.minute, 45);
        assert_eq!(t.period, "SECOND_HALF");
    }

    #[test]
    fn match_time_deserialize_rejects_invalid_period() {
        let json = r#"{"minute":10,"second":30,"period":"EXTRA_TIME"}"#;
        let res: Result<MatchTime, _> = serde_json::from_str(json);
        assert!(res.is_err(), "deserialization of invalid period should fail");
    }
}
