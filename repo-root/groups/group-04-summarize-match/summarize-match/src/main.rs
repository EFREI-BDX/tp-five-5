use axum::{Json, Router, routing::get};
use sea_orm::Database;
use serde::Serialize;
use std::{env, net::SocketAddr, path::Path};
use summarize_match::application::{MatchReadService, MatchSummaryService};
use summarize_match::infrastructure::db_migrator::DatabaseMigrator;
use summarize_match::infrastructure::inbound::consumer::Consumer;
use summarize_match::infrastructure::inbound::http::{event_routes, query_routes};
use summarize_match::infrastructure::outbound::LoggingPublisher;
use summarize_match::infrastructure::repositories::SeaOrmMatchRepository;
use tracing_subscriber::{layer::SubscriberExt, util::SubscriberInitExt};

#[derive(Serialize)]
struct HealthResponse {
    status: &'static str,
    service: &'static str,
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    dotenvy::dotenv().ok();
    init_tracing();

    let database_url =
        env::var("DATABASE_URL").map_err(|_| "Missing DATABASE_URL in environment (.env)")?;

    let port: u16 = env::var("PORT")
        .unwrap_or_else(|_| "3000".to_owned())
        .parse()
        .map_err(|_| "PORT must be a valid u16")?;

    let db = Database::connect(&database_url).await?;

    DatabaseMigrator::new(db.clone()).run().await?;

    let write_repo = SeaOrmMatchRepository::new(db.clone());
    let read_repo = SeaOrmMatchRepository::new(db);

    let command_service = MatchSummaryService::with_publisher(write_repo, LoggingPublisher);
    let query_service = MatchReadService::new(read_repo);

    let consumer = Consumer::new(command_service, base_event_schema_path());

    let app = Router::new()
        .route("/health", get(health))
        .merge(event_routes(consumer))
        .merge(query_routes(query_service));

    let addr = SocketAddr::from(([0, 0, 0, 0], port));
    let listener = tokio::net::TcpListener::bind(addr).await?;

    tracing::info!(%addr, "summarize-match API listening");
    axum::serve(listener, app).await?;

    Ok(())
}

fn init_tracing() {
    tracing_subscriber::registry()
        .with(
            tracing_subscriber::EnvFilter::try_from_default_env()
                .unwrap_or_else(|_| "summarize_match=info".into()),
        )
        .with(tracing_subscriber::fmt::layer())
        .init();
}

async fn health() -> Json<HealthResponse> {
    Json(HealthResponse {
        status: "ok",
        service: "summarize-match",
    })
}

fn base_event_schema_path() -> String {
    env::var("BASE_EVENT_SCHEMA_PATH").unwrap_or_else(|_| {
        Path::new(env!("CARGO_MANIFEST_DIR"))
            .parent()
            .expect("crate should be inside group folder")
            .join("tests/schemas/BaseEvent.schema.json")
            .to_string_lossy()
            .into_owned()
    })
}
