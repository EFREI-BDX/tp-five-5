use crate::application::{ApplicationResult, DomainEventPublisher, PlayerDataPort};
use crate::domain::{DomainEvent, PlayerData};
use async_trait::async_trait;

/// Infrastructure adapter that logs all outbound events as structured JSON.
/// Suitable for local development and debugging. Replace with a real broker adapter in production.
pub struct LoggingPublisher;

#[async_trait]
impl DomainEventPublisher for LoggingPublisher {
    async fn publish(&self, event: &DomainEvent) -> ApplicationResult<()> {
        match serde_json::to_string(event) {
            Ok(json) => tracing::info!(
                event_type = event.event_type(),
                match_id = event.match_id(),
                payload = %json,
                "domain event forwarded"
            ),
            Err(e) => tracing::warn!(error = %e, "failed to serialize domain event for logging"),
        }
        Ok(())
    }
}

#[async_trait]
impl PlayerDataPort for LoggingPublisher {
    async fn publish(&self, events: &[PlayerData]) -> ApplicationResult<()> {
        for data in events {
            match serde_json::to_string(data) {
                Ok(json) => tracing::info!(
                    player_id = %data.player_id.0,
                    payload = %json,
                    "PlayerData event produced"
                ),
                Err(e) => tracing::warn!(error = %e, "failed to serialize PlayerData for logging"),
            }
        }
        Ok(())
    }
}
