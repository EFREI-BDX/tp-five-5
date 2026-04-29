use async_trait::async_trait;
use jsonschema::JSONSchema;
use std::sync::{Arc, Mutex};
use summarize_match::application::{ApplicationResult, MatchSummaryService, NoOpPublisher, PlayerDataPort};
use summarize_match::domain::PlayerData;
use summarize_match::infrastructure::consumer::{Consumer, clear_schema_cache};
use summarize_match::infrastructure::repositories::InMemoryMatchRepository;

fn group_root() -> std::path::PathBuf {
    std::path::Path::new(env!("CARGO_MANIFEST_DIR"))
        .parent()
        .expect("crate should be inside group folder")
        .to_path_buf()
}

fn schema_path() -> String {
    group_root()
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned()
}

fn player_data_schema() -> JSONSchema {
    let path = group_root().join("tests/schemas/player-data.schema.json");
    let content = std::fs::read_to_string(&path).expect("player-data.schema.json should be readable");
    let mut value: serde_json::Value = serde_json::from_str(&content).expect("schema should be valid JSON");
    // jsonschema 0.16 rejects relative $id URLs — strip it before compiling.
    if let Some(obj) = value.as_object_mut() {
        obj.remove("$id");
    }
    let static_ref: &'static serde_json::Value = Box::leak(Box::new(value));
    JSONSchema::compile(static_ref).expect("player-data schema should compile")
}

fn fixture(name: &str) -> String {
    std::fs::read_to_string(group_root().join(format!("tests/fixtures/{}", name)))
        .unwrap_or_else(|e| panic!("read fixture {}: {}", name, e))
}

#[derive(Clone, Default)]
struct CapturingPlayerDataPort {
    captured: Arc<Mutex<Vec<PlayerData>>>,
}

impl CapturingPlayerDataPort {
    fn take(&self) -> Vec<PlayerData> {
        self.captured.lock().unwrap().drain(..).collect()
    }
}

#[async_trait]
impl PlayerDataPort for CapturingPlayerDataPort {
    async fn publish(&self, events: &[PlayerData]) -> ApplicationResult<()> {
        self.captured.lock().unwrap().extend_from_slice(events);
        Ok(())
    }
}

#[tokio::test]
async fn match_finished_produces_valid_player_data_events() {
    clear_schema_cache();
    let port = CapturingPlayerDataPort::default();
    let port_clone = port.clone();

    let service = MatchSummaryService::with_all(
        InMemoryMatchRepository::new(),
        NoOpPublisher,
        port_clone,
    );
    let consumer = Consumer::new(service, schema_path());

    consumer
        .process_json(&fixture("match-started.valid.json"))
        .await
        .expect("match-started should be accepted");
    consumer
        .process_json(&fixture("goal-scored.valid.json"))
        .await
        .expect("goal-scored should be accepted");
    consumer
        .process_json(&fixture("match-finished.valid.json"))
        .await
        .expect("match-finished should be accepted");

    let events = port.take();
    assert_eq!(events.len(), 10, "10 players should produce 10 PlayerData events");

    let schema = player_data_schema();
    for data in &events {
        let json = serde_json::to_value(data).expect("PlayerData should serialize");
        let result = schema.validate(&json);
        assert!(
            result.is_ok(),
            "PlayerData failed schema validation: {:?}\nJSON: {}",
            result.unwrap_err().collect::<Vec<_>>(),
            serde_json::to_string_pretty(&json).unwrap()
        );
    }
}

#[tokio::test]
async fn match_finished_player_data_correctness() {
    clear_schema_cache();
    let port = CapturingPlayerDataPort::default();
    let port_clone = port.clone();

    let service = MatchSummaryService::with_all(
        InMemoryMatchRepository::new(),
        NoOpPublisher,
        port_clone,
    );
    let consumer = Consumer::new(service, schema_path());

    consumer.process_json(&fixture("match-started.valid.json")).await.unwrap();
    consumer.process_json(&fixture("goal-scored.valid.json")).await.unwrap();
    consumer.process_json(&fixture("match-finished.valid.json")).await.unwrap();

    let events = port.take();

    // Scorer: player 2 — 1 goal, bestScorer, Win
    let scorer_id = "00000000-0000-0000-0000-000000000002";
    let scorer = events
        .iter()
        .find(|d| d.player_id.0 == scorer_id)
        .expect("scorer should have PlayerData");
    assert_eq!(scorer.goals, 1, "scorer should have 1 goal");
    assert_eq!(scorer.assists, 0, "scorer should have 0 assists");
    assert!(scorer.best_scorer, "scorer should be bestScorer");

    // Assister: player 3 — 1 assist, bestAssistsProvider, Win
    let assist_id = "00000000-0000-0000-0000-000000000003";
    let assister = events
        .iter()
        .find(|d| d.player_id.0 == assist_id)
        .expect("assister should have PlayerData");
    assert_eq!(assister.assists, 1, "assister should have 1 assist");
    assert!(assister.best_assists_provider, "assister should be bestAssistsProvider");

    // Home team players win
    let home_ids = [
        "00000000-0000-0000-0000-000000000001",
        "00000000-0000-0000-0000-000000000002",
        "00000000-0000-0000-0000-000000000005",
    ];
    for id in &home_ids {
        let player = events.iter().find(|d| &d.player_id.0 == id).unwrap();
        assert_eq!(
            player.result,
            summarize_match::domain::MatchResult::Win,
            "home player {} should Win",
            id
        );
    }

    // Away team players lose
    let away_ids = [
        "00000000-0000-0000-0000-000000000006",
        "00000000-0000-0000-0000-000000000007",
    ];
    for id in &away_ids {
        let player = events.iter().find(|d| &d.player_id.0 == id).unwrap();
        assert_eq!(
            player.result,
            summarize_match::domain::MatchResult::Loss,
            "away player {} should Loss",
            id
        );
    }

    // Exactly one MVP
    let mvp_count = events.iter().filter(|d| d.mvp).count();
    assert_eq!(mvp_count, 1, "exactly one MVP");

    // Play time: match ends at minute=40, second=0 → 2400 seconds; no substitutions
    for data in &events {
        assert_eq!(data.play_time, 2400, "all starters should play 2400 seconds");
    }
}

#[tokio::test]
async fn non_finished_match_does_not_produce_player_data() {
    clear_schema_cache();
    let port = CapturingPlayerDataPort::default();
    let port_clone = port.clone();

    let service = MatchSummaryService::with_all(
        InMemoryMatchRepository::new(),
        NoOpPublisher,
        port_clone,
    );
    let consumer = Consumer::new(service, schema_path());

    consumer
        .process_json(&fixture("match-started.valid.json"))
        .await
        .expect("match-started should be accepted");

    let events = port.take();
    assert!(
        events.is_empty(),
        "PlayerData should only be produced on MATCH_FINISHED"
    );
}
