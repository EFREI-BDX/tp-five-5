use crate::domain::{EventId, PlayerId, TeamId};
use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct SaveMadePayload {
    #[serde(rename = "keeperId")]
    pub keeper_id: PlayerId,
    #[serde(rename = "keeperTeamId")]
    pub keeper_team_id: TeamId,
    #[serde(rename = "relatedShotEventId")]
    pub related_shot_event_id: Option<EventId>,
}
