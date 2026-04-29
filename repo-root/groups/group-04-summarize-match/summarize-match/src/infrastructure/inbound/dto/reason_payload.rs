use serde::Deserialize;

#[derive(Debug, Deserialize)]
pub struct ReasonPayload {
    pub reason: String,
}
