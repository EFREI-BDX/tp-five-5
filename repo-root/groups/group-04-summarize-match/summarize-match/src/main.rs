use axum::{Json, Router, extract::State, routing::get};
use sea_orm::{Database, DatabaseConnection};
use serde::Serialize;
use std::{env, net::SocketAddr};
use tracing_subscriber::{layer::SubscriberExt, util::SubscriberInitExt};

#[derive(Clone)]
struct AppState {
    db: DatabaseConnection,
}

#[derive(Serialize)]
struct HealthResponse {
    status: &'static str,
    service: &'static str,
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    dotenvy::dotenv().ok();
    init_tracing();

    let database_url = env::var("DATABASE_URL")
        .map_err(|_| "Missing DATABASE_URL in environment (.env)")?;

    let port: u16 = env::var("PORT")
        .unwrap_or_else(|_| "3000".to_owned())
        .parse()
        .map_err(|_| "PORT must be a valid u16")?;

    let db = Database::connect(&database_url).await?;
    let state = AppState { db };

    let app = Router::new().route("/health", get(health)).with_state(state);

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

async fn health(State(state): State<AppState>) -> Json<HealthResponse> {
    let _db = &state.db;

    Json(HealthResponse {
        status: "ok",
        service: "summarize-match",
    })
}
