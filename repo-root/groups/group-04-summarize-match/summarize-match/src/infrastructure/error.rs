use std::fmt;

#[derive(Debug)]
pub enum ValidationError {
    Io(std::io::Error),
    Json(serde_json::Error),
    Schema(Vec<String>),
    Other(String),
}

impl fmt::Display for ValidationError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            ValidationError::Io(e) => write!(f, "io error: {}", e),
            ValidationError::Json(e) => write!(f, "json error: {}", e),
            ValidationError::Schema(v) => write!(f, "schema validation errors: {}", v.join("; ")),
            ValidationError::Other(s) => write!(f, "other error: {}", s),
        }
    }
}

impl std::error::Error for ValidationError {}

impl From<std::io::Error> for ValidationError {
    fn from(e: std::io::Error) -> Self {
        ValidationError::Io(e)
    }
}

impl From<serde_json::Error> for ValidationError {
    fn from(e: serde_json::Error) -> Self {
        ValidationError::Json(e)
    }
}

impl From<anyhow::Error> for ValidationError {
    fn from(e: anyhow::Error) -> Self {
        ValidationError::Other(e.to_string())
    }
}
