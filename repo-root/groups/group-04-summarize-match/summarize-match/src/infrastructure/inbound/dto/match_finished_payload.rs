use crate::domain::Score;
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct MatchFinishedPayload {
    #[serde(rename = "finalScore")]
    pub final_score: Score,
}
