use super::error_response::validation_error_response;
use super::state::EventHttpState;
use crate::application::ApplicationService;
use axum::{extract::State, http::StatusCode, response::IntoResponse};

pub async fn accept_event<S>(
    State(state): State<EventHttpState<S>>,
    body: String,
) -> impl IntoResponse
where
    S: ApplicationService,
{
    match state.consumer().process_json(&body).await {
        Ok(()) => StatusCode::ACCEPTED.into_response(),
        Err(error) => validation_error_response(error).into_response(),
    }
}
