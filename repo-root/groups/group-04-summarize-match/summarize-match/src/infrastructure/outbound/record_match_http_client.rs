use crate::application::{ApplicationError, ApplicationResult, RecordMatchProvider};
use crate::domain::{
    PlayerId, RecordAction, RecordMatchEvent, RecordMatchFeed, RecordPlayer, TeamId,
};
use async_trait::async_trait;
use serde_json::Value;

#[derive(Clone)]
pub struct HttpRecordMatchClient {
    base_url: String,
    client: reqwest::Client,
}

impl HttpRecordMatchClient {
    pub fn new(base_url: impl Into<String>) -> Self {
        Self {
            base_url: base_url.into().trim_end_matches('/').to_string(),
            client: reqwest::Client::new(),
        }
    }
}

#[async_trait]
impl RecordMatchProvider for HttpRecordMatchClient {
    async fn fetch_match(&self, match_id: &str) -> ApplicationResult<Option<RecordMatchFeed>> {
        let match_url = format!("{}/v1/matches/{}", self.base_url, match_id);
        let match_response = self
            .client
            .get(match_url)
            .send()
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        if match_response.status() == reqwest::StatusCode::NOT_FOUND {
            return Ok(None);
        }
        if !match_response.status().is_success() {
            return Err(ApplicationError::repository(format!(
                "record-match returned {} for match {}",
                match_response.status(),
                match_id
            )));
        }

        let match_json = match_response
            .json::<Value>()
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        let events_json = if let Some(events) = first_array(&match_json, &["events", "matchEvents"])
        {
            Value::Array(events.clone())
        } else {
            let events_url = format!("{}/v1/matches/{}/events", self.base_url, match_id);
            let events_response = self
                .client
                .get(events_url)
                .send()
                .await
                .map_err(|error| ApplicationError::repository(error.to_string()))?;

            if events_response.status() == reqwest::StatusCode::NOT_FOUND {
                return Ok(None);
            }
            if !events_response.status().is_success() {
                return Err(ApplicationError::repository(format!(
                    "record-match returned {} for match {} events",
                    events_response.status(),
                    match_id
                )));
            }

            events_response
                .json::<Value>()
                .await
                .map_err(|error| ApplicationError::repository(error.to_string()))?
        };

        Ok(Some(parse_feed(match_id, &match_json, &events_json)))
    }
}

fn parse_feed(match_id: &str, match_json: &Value, events_json: &Value) -> RecordMatchFeed {
    let home_team_id = first_string(
        match_json,
        &[
            "homeTeamId",
            "teamHomeId",
            "team1Id",
            "teamAId",
            "teamIdHome",
        ],
    )
    .map(|value| TeamId(value.to_string()));
    let away_team_id = first_string(
        match_json,
        &[
            "awayTeamId",
            "teamAwayId",
            "team2Id",
            "teamBId",
            "teamIdAway",
        ],
    )
    .map(|value| TeamId(value.to_string()));

    let players = first_array(match_json, &["players", "participants"])
        .into_iter()
        .flatten()
        .filter_map(parse_player)
        .collect();

    let events = events_array(events_json)
        .into_iter()
        .filter_map(parse_event)
        .collect();

    RecordMatchFeed {
        match_id: first_string(match_json, &["matchId", "id"])
            .unwrap_or(match_id)
            .to_string(),
        home_team_id,
        away_team_id,
        players,
        events,
    }
}

fn parse_player(value: &Value) -> Option<RecordPlayer> {
    Some(RecordPlayer {
        player_id: PlayerId(first_string(value, &["playerId", "id"])?.to_string()),
        team_id: TeamId(first_string(value, &["teamId"])?.to_string()),
    })
}

fn parse_event(value: &Value) -> Option<RecordMatchEvent> {
    let event_id = first_string(value, &["matchEventId", "eventId", "id"])?.to_string();
    let action = first_string(value, &["action", "name", "type", "eventName"])
        .or_else(|| first_string(value.pointer("/event").unwrap_or(&Value::Null), &["name"]))
        .map(RecordAction::from_record_name)
        .unwrap_or(RecordAction::Unknown);

    Some(RecordMatchEvent {
        event_id,
        action,
        actor_player_id: first_string(
            value,
            &["actorPlayerId", "primaryPlayerId", "player1Id", "playerId"],
        )
        .map(|value| PlayerId(value.to_string())),
        actor_team_id: first_string(
            value,
            &["actorTeamId", "primaryTeamId", "player1TeamId", "teamId"],
        )
        .map(|value| TeamId(value.to_string())),
        secondary_player_id: first_string(
            value,
            &["victimPlayerId", "secondaryPlayerId", "player2Id"],
        )
        .map(|value| PlayerId(value.to_string())),
        secondary_team_id: first_string(
            value,
            &["victimTeamId", "secondaryTeamId", "player2TeamId"],
        )
        .map(|value| TeamId(value.to_string())),
    })
}

fn events_array(value: &Value) -> Vec<&Value> {
    if let Some(array) = value.as_array() {
        return array.iter().collect();
    }
    first_array(value, &["events", "matchEvents", "items"])
        .map(|array| array.iter().collect())
        .unwrap_or_default()
}

fn first_array<'a>(value: &'a Value, keys: &[&str]) -> Option<&'a Vec<Value>> {
    keys.iter()
        .find_map(|key| value.get(*key).and_then(Value::as_array))
}

fn first_string<'a>(value: &'a Value, keys: &[&str]) -> Option<&'a str> {
    keys.iter()
        .find_map(|key| value.get(*key).and_then(Value::as_str))
}

#[cfg(test)]
mod tests {
    use super::parse_event;
    use crate::domain::RecordAction;

    #[test]
    fn parses_group_03_match_event_payload_with_nested_event() {
        let payload = serde_json::json!({
            "matchEventId": "12345678-90ab-cdef-1234-567890abcdef",
            "matchId": "8a7b6c5d-4e3f-2a1b-9c0d-1234567890ab",
            "event": {
                "eventId": "11111111-2222-3333-4444-555555555555",
                "name": "Remplacement",
                "nbPlayers": 2
            },
            "player1Id": "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee",
            "player2Id": "ffffffff-1111-2222-3333-444444444444",
            "occuredAt": "2026-04-15"
        });

        let event = parse_event(&payload).expect("payload should parse");

        assert_eq!(event.event_id, "12345678-90ab-cdef-1234-567890abcdef");
        assert_eq!(event.action, RecordAction::Substitution);
        assert_eq!(
            event.actor_player_id.expect("player1Id should map").0,
            "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
        );
        assert_eq!(
            event.secondary_player_id.expect("player2Id should map").0,
            "ffffffff-1111-2222-3333-444444444444"
        );
    }
}
