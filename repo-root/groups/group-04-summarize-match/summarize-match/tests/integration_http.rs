use axum::body::Body;
use axum::http::{Request, StatusCode};
use summarize_match::application::{MatchReadService, MatchSummaryService};
use summarize_match::infrastructure::inbound::consumer::{Consumer, clear_schema_cache};
use summarize_match::infrastructure::inbound::http::{event_routes, query_routes};
use summarize_match::infrastructure::repositories::InMemoryMatchRepository;
use tower::ServiceExt;

fn group_root() -> std::path::PathBuf {
    std::path::Path::new(env!("CARGO_MANIFEST_DIR"))
        .parent()
        .expect("crate should be inside group folder")
        .to_path_buf()
}

fn app() -> axum::Router {
    let schema_path = group_root()
        .join("tests/schemas/BaseEvent.schema.json")
        .to_string_lossy()
        .into_owned();

    let write_repo = InMemoryMatchRepository::new();
    let read_repo = write_repo.clone();
    let service = MatchSummaryService::new(write_repo);
    let query_service = MatchReadService::new(read_repo);
    let consumer = Consumer::new(service, schema_path);
    event_routes(consumer).merge(query_routes(query_service))
}

fn fixture(name: &str) -> String {
    std::fs::read_to_string(group_root().join(format!("tests/fixtures/{}", name)))
        .unwrap_or_else(|e| panic!("read fixture {}: {}", name, e))
}

#[tokio::test]
async fn post_events_accepts_valid_event() {
    clear_schema_cache();
    let response = app()
        .oneshot(
            Request::builder()
                .method("POST")
                .uri("/events")
                .header("content-type", "application/json")
                .body(Body::from(fixture("match-started.valid.json")))
                .expect("request should build"),
        )
        .await
        .expect("request should be handled");

    assert_eq!(response.status(), StatusCode::ACCEPTED);
}

#[tokio::test]
async fn post_events_rejects_invalid_schema() {
    clear_schema_cache();
    let response = app()
        .oneshot(
            Request::builder()
                .method("POST")
                .uri("/events")
                .header("content-type", "application/json")
                .body(Body::from(fixture("match-started.invalid.json")))
                .expect("request should build"),
        )
        .await
        .expect("request should be handled");

    assert_eq!(response.status(), StatusCode::BAD_REQUEST);
}

#[tokio::test]
async fn post_events_replays_state_and_rejects_duplicate_start() {
    clear_schema_cache();
    let app = app();
    let fixture = fixture("match-started.valid.json");

    let first = app
        .clone()
        .oneshot(
            Request::builder()
                .method("POST")
                .uri("/events")
                .header("content-type", "application/json")
                .body(Body::from(fixture.clone()))
                .expect("request should build"),
        )
        .await
        .expect("first request should be handled");
    assert_eq!(first.status(), StatusCode::ACCEPTED);

    let second = app
        .oneshot(
            Request::builder()
                .method("POST")
                .uri("/events")
                .header("content-type", "application/json")
                .body(Body::from(fixture))
                .expect("request should build"),
        )
        .await
        .expect("second request should be handled");

    assert_eq!(second.status(), StatusCode::UNPROCESSABLE_ENTITY);
}

// ── GET /matches/{matchId}/summary ────────────────────────────────────────────

const MATCH_ID: &str = "11111111-2222-3333-4444-555555555555";

#[tokio::test]
async fn get_summary_returns_404_for_unknown_match() {
    clear_schema_cache();
    let response = app()
        .oneshot(
            Request::builder()
                .method("GET")
                .uri(format!("/matches/{}/summary", MATCH_ID))
                .body(Body::empty())
                .expect("request should build"),
        )
        .await
        .expect("request should be handled");

    assert_eq!(response.status(), StatusCode::NOT_FOUND);
}

#[tokio::test]
async fn get_summary_returns_400_for_invalid_uuid() {
    clear_schema_cache();
    let response = app()
        .oneshot(
            Request::builder()
                .method("GET")
                .uri("/matches/not-a-uuid/summary")
                .body(Body::empty())
                .expect("request should build"),
        )
        .await
        .expect("request should be handled");

    assert_eq!(response.status(), StatusCode::BAD_REQUEST);
}

#[tokio::test]
async fn get_summary_returns_200_after_match_started() {
    clear_schema_cache();
    let app = app();

    let post = app
        .clone()
        .oneshot(
            Request::builder()
                .method("POST")
                .uri("/events")
                .header("content-type", "application/json")
                .body(Body::from(fixture("match-started.valid.json")))
                .expect("request should build"),
        )
        .await
        .expect("post should be handled");
    assert_eq!(post.status(), StatusCode::ACCEPTED);

    let get = app
        .oneshot(
            Request::builder()
                .method("GET")
                .uri(format!("/matches/{}/summary", MATCH_ID))
                .body(Body::empty())
                .expect("request should build"),
        )
        .await
        .expect("get should be handled");
    assert_eq!(get.status(), StatusCode::OK);

    let body = axum::body::to_bytes(get.into_body(), usize::MAX)
        .await
        .expect("body should be readable");
    let json: serde_json::Value = serde_json::from_slice(&body).expect("body should be JSON");
    assert_eq!(json["matchId"], MATCH_ID);
    assert_eq!(json["status"], "IN_PROGRESS");
    assert_eq!(json["score"]["home"], 0);
    assert_eq!(json["score"]["away"], 0);
}

#[tokio::test]
async fn get_summary_reflects_goal_and_finished() {
    clear_schema_cache();
    let app = app();

    for event in &["match-started.valid.json", "goal-scored.valid.json", "match-finished.valid.json"] {
        let resp = app
            .clone()
            .oneshot(
                Request::builder()
                    .method("POST")
                    .uri("/events")
                    .header("content-type", "application/json")
                    .body(Body::from(fixture(event)))
                    .expect("request should build"),
            )
            .await
            .expect("post should be handled");
        assert_eq!(resp.status(), StatusCode::ACCEPTED, "POST {} failed", event);
    }

    let get = app
        .oneshot(
            Request::builder()
                .method("GET")
                .uri(format!("/matches/{}/summary", MATCH_ID))
                .body(Body::empty())
                .expect("request should build"),
        )
        .await
        .expect("get should be handled");
    assert_eq!(get.status(), StatusCode::OK);

    let body = axum::body::to_bytes(get.into_body(), usize::MAX)
        .await
        .expect("body should be readable");
    let json: serde_json::Value = serde_json::from_slice(&body).expect("body should be JSON");
    assert_eq!(json["status"], "FINISHED");
    assert_eq!(json["score"]["home"], 1);
    assert_eq!(json["score"]["away"], 0);
    assert_eq!(json["goals"].as_array().unwrap().len(), 1);
}
