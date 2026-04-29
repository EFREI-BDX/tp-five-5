use super::ApplicationResult;
use crate::domain::{DomainEvent, MatchAggregate};
use async_trait::async_trait;

#[async_trait]
pub trait MatchRepository: Send + Sync + 'static {
    async fn load(&self, match_id: &str) -> ApplicationResult<MatchAggregate>;
    async fn append(&self, event: DomainEvent) -> ApplicationResult<()>;
}
