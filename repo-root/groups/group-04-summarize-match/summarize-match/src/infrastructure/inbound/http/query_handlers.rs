use super::query_state::QueryHttpState;
use crate::application::{ApplicationError, MatchQueryService};
use axum::{
    Json,
    extract::{Path, State},
    http::StatusCode,
    response::IntoResponse,
};
use serde::Serialize;
use uuid::Uuid;

#[derive(Serialize)]
struct ErrorBody {
    error: &'static str,
    message: String,
}

pub async fn get_match_summary<Q: MatchQueryService>(
    State(state): State<QueryHttpState<Q>>,
    Path(match_id): Path<String>,
) -> impl IntoResponse {
    if Uuid::parse_str(&match_id).is_err() {
        return (
            StatusCode::BAD_REQUEST,
            Json(ErrorBody {
                error: "InvalidMatchId",
                message: format!("'{}' is not a valid UUID", match_id),
            }),
        )
            .into_response();
    }

    match state.query_service().get_summary(&match_id).await {
        Ok(Some(summary)) => (StatusCode::OK, Json(summary)).into_response(),
        Ok(None) => (
            StatusCode::NOT_FOUND,
            Json(ErrorBody {
                error: "NotFound",
                message: format!("match {} not found", match_id),
            }),
        )
            .into_response(),
        Err(ApplicationError::Repository(msg)) => (
            StatusCode::INTERNAL_SERVER_ERROR,
            Json(ErrorBody {
                error: "InfrastructureError",
                message: msg,
            }),
        )
            .into_response(),
        Err(ApplicationError::DomainRuleViolation(msg)) => (
            StatusCode::UNPROCESSABLE_ENTITY,
            Json(ErrorBody {
                error: "DomainError",
                message: msg,
            }),
        )
            .into_response(),
    }
}
