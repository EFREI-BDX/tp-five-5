use super::ApplicationResult;
use crate::domain::DomainEvent;
use async_trait::async_trait;

#[async_trait]
pub trait DomainEventPublisher: Send + Sync + 'static {
    async fn publish(&self, event: &DomainEvent) -> ApplicationResult<()>;
}

pub struct NoOpPublisher;

#[async_trait]
impl DomainEventPublisher for NoOpPublisher {
    async fn publish(&self, _event: &DomainEvent) -> ApplicationResult<()> {
        Ok(())
    }
}
