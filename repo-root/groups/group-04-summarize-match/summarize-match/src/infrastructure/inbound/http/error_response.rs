use crate::infrastructure::error::ValidationError;
use axum::{Json, http::StatusCode, response::IntoResponse};
use serde::Serialize;

#[derive(Serialize)]
struct ErrorResponse {
    error: &'static str,
    message: String,
    #[serde(skip_serializing_if = "Vec::is_empty")]
    details: Vec<String>,
}

pub fn validation_error_response(error: ValidationError) -> impl IntoResponse {
    let (status, response) = match error {
        ValidationError::Json(error) => (
            StatusCode::BAD_REQUEST,
            ErrorResponse {
                error: "InvalidJson",
                message: error.to_string(),
                details: Vec::new(),
            },
        ),
        ValidationError::Schema(details) => (
            StatusCode::BAD_REQUEST,
            ErrorResponse {
                error: "InvalidEventSchema",
                message: "event does not match its JSON Schema".to_string(),
                details,
            },
        ),
        ValidationError::Io(error) => (
            StatusCode::INTERNAL_SERVER_ERROR,
            ErrorResponse {
                error: "InfrastructureError",
                message: error.to_string(),
                details: Vec::new(),
            },
        ),
        ValidationError::Other(message) => (
            StatusCode::UNPROCESSABLE_ENTITY,
            ErrorResponse {
                error: "EventRejected",
                message,
                details: Vec::new(),
            },
        ),
    };

    (status, Json(response))
}
