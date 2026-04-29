use super::{ApplicationResult, MatchRepository};
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
        self.repository.read_summary(match_id).await
    }

    async fn get_team_stats(
        &self,
        match_id: &str,
        team_id: &TeamId,
    ) -> ApplicationResult<Option<TeamStats>> {
        let aggregate = self.repository.load(match_id).await?;
        if !aggregate.is_known() {
            return Ok(None);
        }
        Ok(aggregate.to_team_stats(match_id, team_id))
    }

    async fn get_player_stats(
        &self,
        match_id: &str,
        player_id: &PlayerId,
    ) -> ApplicationResult<Option<PlayerStats>> {
        let aggregate = self.repository.load(match_id).await?;
        if !aggregate.is_known() {
            return Ok(None);
        }
        Ok(aggregate.to_player_stats(match_id, player_id))
    }
}
