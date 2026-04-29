use super::query_state::QueryHttpState;
use crate::application::{ApplicationError, MatchQueryService};
use crate::domain::{PlayerId, TeamId};
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

pub async fn get_team_stats<Q: MatchQueryService>(
    State(state): State<QueryHttpState<Q>>,
    Path((match_id, team_id)): Path<(String, String)>,
) -> impl IntoResponse {
    if let Err(response) = validate_uuid("matchId", &match_id) {
        return response;
    }
    if let Err(response) = validate_uuid("teamId", &team_id) {
        return response;
    }

    match state
        .query_service()
        .get_team_stats(&match_id, &TeamId(team_id.clone()))
        .await
    {
        Ok(Some(stats)) => (StatusCode::OK, Json(stats)).into_response(),
        Ok(None) => not_found(format!(
            "stats for team {} in match {} not found",
            team_id, match_id
        )),
        Err(error) => application_error_response(error),
    }
}

pub async fn get_player_stats<Q: MatchQueryService>(
    State(state): State<QueryHttpState<Q>>,
    Path((match_id, player_id)): Path<(String, String)>,
) -> impl IntoResponse {
    if let Err(response) = validate_uuid("matchId", &match_id) {
        return response;
    }
    if let Err(response) = validate_uuid("playerId", &player_id) {
        return response;
    }

    match state
        .query_service()
        .get_player_stats(&match_id, &PlayerId(player_id.clone()))
        .await
    {
        Ok(Some(stats)) => (StatusCode::OK, Json(stats)).into_response(),
        Ok(None) => not_found(format!(
            "stats for player {} in match {} not found",
            player_id, match_id
        )),
        Err(error) => application_error_response(error),
    }
}

fn validate_uuid(label: &'static str, value: &str) -> Result<(), axum::response::Response> {
    Uuid::parse_str(value).map(|_| ()).map_err(|_| {
        (
            StatusCode::BAD_REQUEST,
            Json(ErrorBody {
                error: "InvalidUuid",
                message: format!("'{}' is not a valid UUID for {}", value, label),
            }),
        )
            .into_response()
    })
}

fn not_found(message: String) -> axum::response::Response {
    (
        StatusCode::NOT_FOUND,
        Json(ErrorBody {
            error: "NotFound",
            message,
        }),
    )
        .into_response()
}

fn application_error_response(error: ApplicationError) -> axum::response::Response {
    match error {
        ApplicationError::Repository(msg) => (
            StatusCode::INTERNAL_SERVER_ERROR,
            Json(ErrorBody {
                error: "InfrastructureError",
                message: msg,
            }),
        )
            .into_response(),
        ApplicationError::DomainRuleViolation(msg) => (
            StatusCode::UNPROCESSABLE_ENTITY,
            Json(ErrorBody {
                error: "DomainError",
                message: msg,
            }),
        )
            .into_response(),
    }
}
