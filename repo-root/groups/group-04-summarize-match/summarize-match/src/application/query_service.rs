use super::{ApplicationResult, MatchRepository};
use crate::domain::MatchSummary;
use async_trait::async_trait;

#[async_trait]
pub trait MatchQueryService: Send + Sync + 'static {
    async fn get_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>>;
}

pub struct MatchReadService<R: MatchRepository> {
    repository: R,
}

impl<R: MatchRepository> MatchReadService<R> {
    pub fn new(repository: R) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl<R: MatchRepository> MatchQueryService for MatchReadService<R> {
    async fn get_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>> {
        let aggregate = self.repository.load(match_id).await?;
        if !aggregate.is_known() {
            return Ok(None);
        }
        Ok(Some(aggregate.to_summary(match_id)))
    }
}
