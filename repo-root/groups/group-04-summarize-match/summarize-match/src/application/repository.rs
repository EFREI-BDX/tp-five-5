use super::ApplicationResult;
use crate::domain::{DomainEvent, MatchAggregate, MatchSummary};
use async_trait::async_trait;

#[async_trait]
pub trait MatchRepository: Send + Sync + 'static {
    async fn load(&self, match_id: &str) -> ApplicationResult<MatchAggregate>;
    async fn append(&self, event: DomainEvent) -> ApplicationResult<()>;
    async fn read_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>>;
}
