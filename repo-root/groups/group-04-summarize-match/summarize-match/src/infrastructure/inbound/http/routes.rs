use super::handlers::accept_event;
use super::query_handlers::{get_match_summary, get_player_stats, get_team_stats};
use super::query_state::QueryHttpState;
use super::state::EventHttpState;
use crate::application::{ApplicationService, MatchQueryService};
use crate::infrastructure::inbound::consumer::Consumer;
use axum::{Router, routing::{get, post}};

pub fn event_routes<S>(consumer: Consumer<S>) -> Router
where
    S: ApplicationService,
{
    Router::new()
        .route("/events", post(accept_event::<S>))
        .with_state(EventHttpState::new(consumer))
}

pub fn query_routes<Q>(query_service: Q) -> Router
where
    Q: MatchQueryService,
{
    Router::new()
        .route("/matches/{match_id}/summary", get(get_match_summary::<Q>))
        .route(
            "/matches/{match_id}/teams/{team_id}/stats",
            get(get_team_stats::<Q>),
        )
        .route(
            "/matches/{match_id}/players/{player_id}/stats",
            get(get_player_stats::<Q>),
        )
        .with_state(QueryHttpState::new(query_service))
}
