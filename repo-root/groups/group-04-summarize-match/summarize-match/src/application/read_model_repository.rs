use super::ApplicationResult;
use crate::domain::{MatchSummary, PlayerMatchStats};
use async_trait::async_trait;

#[derive(Debug, Clone)]
pub struct CachedMatchReadModels {
    pub summary: MatchSummary,
    pub player_stats: Vec<PlayerMatchStats>,
}

#[async_trait]
pub trait MatchReadModelRepository: Send + Sync + 'static {
    async fn load_read_models(
        &self,
        match_id: &str,
    ) -> ApplicationResult<Option<CachedMatchReadModels>>;

    async fn save_read_models(
        &self,
        match_id: &str,
        summary: &MatchSummary,
        player_stats: &[PlayerMatchStats],
    ) -> ApplicationResult<()>;
}
