use super::ApplicationResult;
use crate::domain::RecordMatchFeed;
use async_trait::async_trait;

#[async_trait]
pub trait RecordMatchProvider: Send + Sync + 'static {
    async fn fetch_match(&self, match_id: &str) -> ApplicationResult<Option<RecordMatchFeed>>;
}
