use summarize_match::application::ApplicationService;
use summarize_match::application::MatchSummaryService;
use summarize_match::infrastructure::consumer::{clear_schema_cache, schema_cache_len, Consumer};
use summarize_match::domain::DomainEvent;
use async_trait::async_trait;
use std::sync::{Arc, Mutex};

struct TestService {
    received: Arc<Mutex<Option<DomainEvent>>>,
}

impl TestService {
    fn new() -> Self {
        Self {
            received: Arc::new(Mutex::new(None)),
        }
    }
}

#[async_trait]
impl ApplicationService for TestService {
    async fn handle_event(&self, event: DomainEvent) -> anyhow::Result<()> {
        let mut guard = self.received.lock().unwrap();
        *guard = Some(event);
        Ok(())
    }
}

#[tokio::test]
async fn consumer_validates_and_forwards_event() {
    // Use CARGO_MANIFEST_DIR to build deterministic paths to test fixtures/schemas
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    // fixtures live at the group root tests/fixtures, one level above the crate directory
    let group_root = crate_dir.parent().expect("crate should be inside group folder");
    let fixture_path = group_root.join("tests/fixtures/match-started.valid.json");
    let fixture = std::fs::read_to_string(&fixture_path).expect("read fixture");
    let schema_path = group_root.join("tests/schemas/BaseEvent.schema.json").to_string_lossy().into_owned();

    let service = TestService::new();
    let received = service.received.clone();

    // Ensure cache is clear for test isolation
    clear_schema_cache();
    assert_eq!(schema_cache_len(), 0);

    let consumer = Consumer::new(service, schema_path);
    consumer.process_json(&fixture).await.expect("process should succeed");

    let guard = received.lock().unwrap();
    assert!(guard.is_some(), "service should have received an event");
    match guard.as_ref().unwrap() {
        DomainEvent::MatchStarted(ms) => {
            assert_eq!(ms.scheduled_duration_minutes, 40);
        }
        _ => panic!("expected MatchStarted event"),
    }
}

#[tokio::test]
async fn consumer_rejects_invalid_event() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir.parent().expect("crate should be inside group folder");
    let fixture_path = group_root.join("tests/fixtures/match-started.invalid.json");
    let fixture = std::fs::read_to_string(&fixture_path).expect("read fixture");
    let schema_path = group_root.join("tests/schemas/BaseEvent.schema.json").to_string_lossy().into_owned();

    let service = TestService::new();

    clear_schema_cache();

    let consumer = Consumer::new(service, schema_path);
    let res = consumer.process_json(&fixture).await;
    assert!(res.is_err(), "process should fail for invalid fixture");
}

#[tokio::test]
async fn consumer_with_real_application_service_rejects_duplicate_match_started() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir.parent().expect("crate should be inside group folder");
    let fixture_path = group_root.join("tests/fixtures/match-started.valid.json");
    let fixture = std::fs::read_to_string(&fixture_path).expect("read fixture");
    let schema_path = group_root.join("tests/schemas/BaseEvent.schema.json").to_string_lossy().into_owned();

    clear_schema_cache();

    let service = MatchSummaryService::new();
    let consumer = Consumer::new(service, schema_path);

    consumer
        .process_json(&fixture)
        .await
        .expect("first MATCH_STARTED should pass");

    let second = consumer.process_json(&fixture).await;
    assert!(second.is_err(), "second MATCH_STARTED should fail by domain rule");
}

#[tokio::test]
async fn consumer_sequence_goal_and_finished_is_valid_when_scores_match() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir.parent().expect("crate should be inside group folder");

    let started = std::fs::read_to_string(group_root.join("tests/fixtures/match-started.valid.json"))
        .expect("read match-started fixture");
    let goal = std::fs::read_to_string(group_root.join("tests/fixtures/goal-scored.valid.json"))
        .expect("read goal-scored fixture");
    let finished = std::fs::read_to_string(group_root.join("tests/fixtures/match-finished.valid.json"))
        .expect("read match-finished fixture");

    let schema_path = group_root.join("tests/schemas/BaseEvent.schema.json").to_string_lossy().into_owned();

    clear_schema_cache();

    let service = MatchSummaryService::new();
    let consumer = Consumer::new(service, schema_path);

    consumer.process_json(&started).await.expect("start should pass");
    consumer.process_json(&goal).await.expect("goal should pass");
    consumer
        .process_json(&finished)
        .await
        .expect("finish should pass when score matches computed goals");
}

#[tokio::test]
async fn consumer_sequence_finished_fails_when_score_mismatch() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir.parent().expect("crate should be inside group folder");

    let started = std::fs::read_to_string(group_root.join("tests/fixtures/match-started.valid.json"))
        .expect("read match-started fixture");
    let goal = std::fs::read_to_string(group_root.join("tests/fixtures/goal-scored.valid.json"))
        .expect("read goal-scored fixture");
    let finished_bad =
        std::fs::read_to_string(group_root.join("tests/fixtures/match-finished.invalid-score.json"))
            .expect("read bad match-finished fixture");

    let schema_path = group_root.join("tests/schemas/BaseEvent.schema.json").to_string_lossy().into_owned();

    clear_schema_cache();

    let service = MatchSummaryService::new();
    let consumer = Consumer::new(service, schema_path);

    consumer.process_json(&started).await.expect("start should pass");
    consumer.process_json(&goal).await.expect("goal should pass");
    let result = consumer.process_json(&finished_bad).await;
    assert!(result.is_err(), "finish should fail when score mismatches computed goals");
}
