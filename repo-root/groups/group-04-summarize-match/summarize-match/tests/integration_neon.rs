use axum::body::Body;
use axum::http::{Request, StatusCode};
use sea_orm::{ConnectionTrait, Database, DatabaseConnection, Statement};
use summarize_match::application::{MatchReadService, MatchSummaryService};
use summarize_match::infrastructure::db_migrator::DatabaseMigrator;
use summarize_match::infrastructure::inbound::consumer::{Consumer, clear_schema_cache};
use summarize_match::infrastructure::inbound::http::{event_routes, query_routes};
use summarize_match::infrastructure::repositories::SeaOrmMatchRepository;
use tower::ServiceExt;
use uuid::Uuid;

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

fn unique_match_started_fixture(match_id: &str) -> String {
    let fixture_path = group_root().join("tests/fixtures/match-started.valid.json");
    let mut event: serde_json::Value = serde_json::from_str(
        &std::fs::read_to_string(&fixture_path).expect("fixture should be readable"),
    )
    .expect("fixture should be valid JSON");

    event["eventId"] = serde_json::Value::String(Uuid::new_v4().to_string());
    event["matchId"] = serde_json::Value::String(match_id.to_string());

    serde_json::to_string(&event).expect("fixture should serialize")
}

async fn persisted_event_count(db: &DatabaseConnection, match_id: &str) -> anyhow::Result<i64> {
    let statement = Statement::from_string(
        db.get_database_backend(),
        format!(
            "SELECT COUNT(*) AS event_count FROM match_events WHERE match_id = '{}'",
            match_id
        ),
    );

    let row = db
        .query_one(statement)
        .await?
        .expect("count query should return a row");

    Ok(row.try_get("", "event_count")?)
}

async fn cleanup_match_events(db: &DatabaseConnection, match_id: &str) -> anyhow::Result<()> {
    db.execute(Statement::from_string(
        db.get_database_backend(),
        format!("DELETE FROM match_events WHERE match_id = '{}'", match_id),
    ))
    .await?;

    Ok(())
}

#[tokio::test]
#[ignore = "requires a reachable Neon DATABASE_URL from summarize-match/.env"]
async fn post_events_persists_and_replays_with_neon_database() -> anyhow::Result<()> {
    dotenvy::dotenv().ok();
    clear_schema_cache();

    let database_url = std::env::var("DATABASE_URL")?;
    let db = Database::connect(&database_url).await?;
    DatabaseMigrator::new(db.clone()).run().await?;
    let repository = SeaOrmMatchRepository::new(db.clone());

    let read_repo = SeaOrmMatchRepository::new(db.clone());
    let service = MatchSummaryService::new(repository);
    let query_service = MatchReadService::new(read_repo);
    let consumer = Consumer::new(service, schema_path());
    let app = event_routes(consumer).merge(query_routes(query_service));

    let match_id = Uuid::new_v4().to_string();
    let fixture = unique_match_started_fixture(&match_id);

    cleanup_match_events(&db, &match_id).await?;

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
    assert_eq!(persisted_event_count(&db, &match_id).await?, 1);

    let second = app
        .clone()
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
    assert_eq!(persisted_event_count(&db, &match_id).await?, 1);

    // GET /matches/{matchId}/summary should return 200 with the persisted state
    let summary_response = app
        .oneshot(
            Request::builder()
                .method("GET")
                .uri(format!("/matches/{}/summary", match_id))
                .body(Body::empty())
                .expect("summary request should build"),
        )
        .await
        .expect("summary request should be handled");

    assert_eq!(summary_response.status(), StatusCode::OK);

    let body = axum::body::to_bytes(summary_response.into_body(), usize::MAX)
        .await
        .expect("summary body should be readable");
    let json: serde_json::Value = serde_json::from_slice(&body).expect("summary should be JSON");
    assert_eq!(json["matchId"], match_id);
    assert_eq!(json["status"], "IN_PROGRESS");

    cleanup_match_events(&db, &match_id).await?;
    Ok(())
}
