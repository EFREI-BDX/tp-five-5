use super::{ApplicationResult, MatchStatsRepository};
use crate::domain::{MatchSummary, PlayerId, PlayerStats, TeamId, TeamStats};
use async_trait::async_trait;

#[async_trait]
pub trait MatchQueryService: Send + Sync + 'static {
    async fn get_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>>;
    async fn get_team_stats(
        &self,
        match_id: &str,
        team_id: &TeamId,
    ) -> ApplicationResult<Option<TeamStats>>;
    async fn get_player_stats(
        &self,
        match_id: &str,
        player_id: &PlayerId,
    ) -> ApplicationResult<Option<PlayerStats>>;
}

pub struct MatchReadService<R: MatchStatsRepository> {
    repository: R,
}

impl<R: MatchStatsRepository> MatchReadService<R> {
    pub fn new(repository: R) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl<R: MatchStatsRepository> MatchQueryService for MatchReadService<R> {
    async fn get_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>> {
        self.repository.read_summary(match_id).await
    }

    async fn get_team_stats(
        &self,
        match_id: &str,
        team_id: &TeamId,
    ) -> ApplicationResult<Option<TeamStats>> {
        self.repository.read_team_stats(match_id, team_id).await
    }

    async fn get_player_stats(
        &self,
        match_id: &str,
        player_id: &PlayerId,
    ) -> ApplicationResult<Option<PlayerStats>> {
        self.repository.read_player_stats(match_id, player_id).await
    }
}
