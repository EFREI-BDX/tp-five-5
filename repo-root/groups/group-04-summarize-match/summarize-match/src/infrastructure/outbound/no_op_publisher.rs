use crate::application::{ApplicationResult, DomainEventPublisher};
use crate::domain::DomainEvent;
use async_trait::async_trait;

/// Infrastructure adapter for DomainEventPublisher that discards all events.
/// Replace with a Kafka/AMQP adapter when downstream consumers are integrated.
pub struct NoOpEventPublisher;

#[async_trait]
impl DomainEventPublisher for NoOpEventPublisher {
    async fn publish(&self, event: &DomainEvent) -> ApplicationResult<()> {
        tracing::debug!(event_type = event.event_type(), "domain event not forwarded (no-op publisher)");
        Ok(())
    }
}
