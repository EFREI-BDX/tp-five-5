use async_trait::async_trait;
use std::sync::{Arc, Mutex};
use summarize_match::application::MatchSummaryService;
use summarize_match::application::{ApplicationResult, ApplicationService};
use summarize_match::domain::DomainEvent;
use summarize_match::infrastructure::consumer::{Consumer, clear_schema_cache, schema_cache_len};
use summarize_match::infrastructure::repositories::InMemoryMatchRepository;

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
    async fn handle_event(&self, event: DomainEvent) -> ApplicationResult<()> {
        let mut guard = self.received.lock().unwrap();
        *guard = Some(event);
        Ok(())
    }
}

fn group_paths() -> (std::path::PathBuf, String) {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir
        .parent()
        .expect("crate should be inside group folder")
        .to_path_buf();
    let schema_path = group_root
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();
    (group_root, schema_path)
}

fn fixture(group_root: &std::path::Path, name: &str) -> String {
    std::fs::read_to_string(group_root.join(format!("tests/fixtures/{}", name)))
        .unwrap_or_else(|e| panic!("read fixture {}: {}", name, e))
}

fn match_summary_service() -> MatchSummaryService<InMemoryMatchRepository> {
    MatchSummaryService::new(InMemoryMatchRepository::new())
}

fn variant_name(event: &DomainEvent) -> &'static str {
    match event {
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

#[tokio::test]
async fn consumer_validates_and_forwards_event() {
    // Use CARGO_MANIFEST_DIR to build deterministic paths to test fixtures/schemas
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    // fixtures live at the group root tests/fixtures, one level above the crate directory
    let group_root = crate_dir
        .parent()
        .expect("crate should be inside group folder");
    let fixture_path = group_root.join("tests/fixtures/match-started.valid.json");
    let fixture = std::fs::read_to_string(&fixture_path).expect("read fixture");
    let schema_path = group_root
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();

    let service = TestService::new();
    let received = service.received.clone();

    // Ensure cache is clear for test isolation
    clear_schema_cache();
    assert_eq!(schema_cache_len(), 0);

    let consumer = Consumer::new(service, schema_path);
    consumer
        .process_json(&fixture)
        .await
        .expect("process should succeed");

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
async fn consumer_validates_and_forwards_all_inbound_events() {
    let (group_root, schema_path) = group_paths();
    let cases = [
        ("match-started.valid.json", "MATCH_STARTED"),
        ("goal-scored.valid.json", "GOAL_SCORED"),
        ("goal-cancelled.valid.json", "GOAL_CANCELLED"),
        ("match-finished.valid.json", "MATCH_FINISHED"),
        ("pass-attempted.valid.json", "PASS_ATTEMPTED"),
        ("shot-attempted.valid.json", "SHOT_ATTEMPTED"),
        ("foul-committed.valid.json", "FOUL_COMMITTED"),
        ("yellow-card.valid.json", "YELLOW_CARD"),
        ("red-card.valid.json", "RED_CARD"),
        ("save-made.valid.json", "SAVE_MADE"),
        ("substitution.valid.json", "SUBSTITUTION"),
        ("match-paused.valid.json", "MATCH_PAUSED"),
        ("match-resumed.valid.json", "MATCH_RESUMED"),
        ("match-cancelled.valid.json", "MATCH_CANCELLED"),
        ("match-forfeited.valid.json", "MATCH_FORFEITED"),
    ];

    for (fixture_name, expected_type) in cases {
        let service = TestService::new();
        let received = service.received.clone();
        clear_schema_cache();
        let consumer = Consumer::new(service, schema_path.clone());
        consumer
            .process_json(&fixture(&group_root, fixture_name))
            .await
            .unwrap_or_else(|e| panic!("{} should process: {}", fixture_name, e));

        let guard = received.lock().unwrap();
        let event = guard.as_ref().expect("service should receive an event");
        assert_eq!(variant_name(event), expected_type);
    }
}

#[tokio::test]
async fn consumer_rejects_invalid_fixtures_for_all_inbound_events() {
    let (group_root, schema_path) = group_paths();
    let invalid_fixtures = [
        "match-started.invalid.json",
        "goal-scored.invalid.json",
        "goal-cancelled.invalid.json",
        "match-finished.invalid.json",
        "pass-attempted.invalid.json",
        "shot-attempted.invalid.json",
        "foul-committed.invalid.json",
        "yellow-card.invalid.json",
        "red-card.invalid.json",
        "save-made.invalid.json",
        "substitution.invalid.json",
        "match-paused.invalid.json",
        "match-resumed.invalid.json",
        "match-cancelled.invalid.json",
        "match-forfeited.invalid.json",
    ];

    for fixture_name in invalid_fixtures {
        clear_schema_cache();
        let consumer = Consumer::new(TestService::new(), schema_path.clone());
        let result = consumer
            .process_json(&fixture(&group_root, fixture_name))
            .await;
        assert!(result.is_err(), "{} should be rejected", fixture_name);
    }
}

#[tokio::test]
async fn consumer_rejects_invalid_event() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir
        .parent()
        .expect("crate should be inside group folder");
    let fixture_path = group_root.join("tests/fixtures/match-started.invalid.json");
    let fixture = std::fs::read_to_string(&fixture_path).expect("read fixture");
    let schema_path = group_root
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();

    let service = TestService::new();

    clear_schema_cache();

    let consumer = Consumer::new(service, schema_path);
    let res = consumer.process_json(&fixture).await;
    assert!(res.is_err(), "process should fail for invalid fixture");
}

#[tokio::test]
async fn consumer_validates_and_forwards_red_card() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir
        .parent()
        .expect("crate should be inside group folder");
    let fixture_path = group_root.join("tests/fixtures/red-card.valid.json");
    let fixture = std::fs::read_to_string(&fixture_path).expect("read fixture");
    let schema_path = group_root
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();

    let service = TestService::new();
    let received = service.received.clone();

    clear_schema_cache();
    let consumer = Consumer::new(service, schema_path);
    consumer
        .process_json(&fixture)
        .await
        .expect("process should succeed");

    let guard = received.lock().unwrap();
    assert!(guard.is_some(), "service should have received an event");
    match guard.as_ref().unwrap() {
        DomainEvent::RedCard(rc) => {
            assert!(rc.is_double_yellow == false);
        }
        _ => panic!("expected RedCard event"),
    }
}

#[tokio::test]
async fn consumer_with_real_application_service_rejects_duplicate_match_started() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir
        .parent()
        .expect("crate should be inside group folder");
    let fixture_path = group_root.join("tests/fixtures/match-started.valid.json");
    let fixture = std::fs::read_to_string(&fixture_path).expect("read fixture");
    let schema_path = group_root
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();

    clear_schema_cache();

    let service = match_summary_service();
    let consumer = Consumer::new(service, schema_path);

    consumer
        .process_json(&fixture)
        .await
        .expect("first MATCH_STARTED should pass");

    let second = consumer.process_json(&fixture).await;
    assert!(
        second.is_err(),
        "second MATCH_STARTED should fail by domain rule"
    );
}

#[tokio::test]
async fn consumer_sequence_goal_and_finished_is_valid_when_scores_match() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir
        .parent()
        .expect("crate should be inside group folder");

    let started =
        std::fs::read_to_string(group_root.join("tests/fixtures/match-started.valid.json"))
            .expect("read match-started fixture");
    let goal = std::fs::read_to_string(group_root.join("tests/fixtures/goal-scored.valid.json"))
        .expect("read goal-scored fixture");
    let finished =
        std::fs::read_to_string(group_root.join("tests/fixtures/match-finished.valid.json"))
            .expect("read match-finished fixture");

    let schema_path = group_root
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();

    clear_schema_cache();

    let service = match_summary_service();
    let consumer = Consumer::new(service, schema_path);

    consumer
        .process_json(&started)
        .await
        .expect("start should pass");
    consumer
        .process_json(&goal)
        .await
        .expect("goal should pass");
    consumer
        .process_json(&finished)
        .await
        .expect("finish should pass when score matches computed goals");
}

#[tokio::test]
async fn consumer_sequence_finished_fails_when_score_mismatch() {
    let manifest = env!("CARGO_MANIFEST_DIR");
    let crate_dir = std::path::Path::new(manifest);
    let group_root = crate_dir
        .parent()
        .expect("crate should be inside group folder");

    let started =
        std::fs::read_to_string(group_root.join("tests/fixtures/match-started.valid.json"))
            .expect("read match-started fixture");
    let goal = std::fs::read_to_string(group_root.join("tests/fixtures/goal-scored.valid.json"))
        .expect("read goal-scored fixture");
    let finished_bad = std::fs::read_to_string(
        group_root.join("tests/fixtures/match-finished.invalid-score.json"),
    )
    .expect("read bad match-finished fixture");

    let schema_path = group_root
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();

    clear_schema_cache();

    let service = match_summary_service();
    let consumer = Consumer::new(service, schema_path);

    consumer
        .process_json(&started)
        .await
        .expect("start should pass");
    consumer
        .process_json(&goal)
        .await
        .expect("goal should pass");
    let result = consumer.process_json(&finished_bad).await;
    assert!(
        result.is_err(),
        "finish should fail when score mismatches computed goals"
    );
}

#[tokio::test]
async fn consumer_sequence_all_non_terminal_events_then_finish_is_valid() {
    let (group_root, schema_path) = group_paths();
    let service = match_summary_service();
    let consumer = Consumer::new(service, schema_path);

    clear_schema_cache();
    for fixture_name in [
        "match-started.valid.json",
        "pass-attempted.valid.json",
        "shot-attempted.valid.json",
        "save-made.valid.json",
        "foul-committed.valid.json",
        "yellow-card.valid.json",
        "goal-scored.valid.json",
        "goal-cancelled.valid.json",
        "substitution.valid.json",
        "match-paused.valid.json",
        "match-resumed.valid.json",
        "red-card.valid.json",
        "match-finished.invalid-score.json",
    ] {
        consumer
            .process_json(&fixture(&group_root, fixture_name))
            .await
            .unwrap_or_else(|e| panic!("{} should pass in sequence: {}", fixture_name, e));
    }
}

#[tokio::test]
async fn consumer_sequence_match_forfeited_is_terminal() {
    let (group_root, schema_path) = group_paths();
    let service = match_summary_service();
    let consumer = Consumer::new(service, schema_path);

    clear_schema_cache();
    consumer
        .process_json(&fixture(&group_root, "match-started.valid.json"))
        .await
        .expect("start should pass");
    consumer
        .process_json(&fixture(&group_root, "match-forfeited.valid.json"))
        .await
        .expect("forfeit should pass");

    let result = consumer
        .process_json(&fixture(&group_root, "pass-attempted.valid.json"))
        .await;
    assert!(result.is_err(), "events after forfeit should fail");
}
