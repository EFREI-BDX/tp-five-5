use super::ApplicationResult;
use crate::domain::{
    DomainEvent, MatchAggregate, MatchSummary, PlayerId, PlayerStats, TeamId, TeamStats,
};
use async_trait::async_trait;

#[async_trait]
pub trait MatchRepository: Send + Sync + 'static {
    async fn load(&self, match_id: &str) -> ApplicationResult<MatchAggregate>;
    async fn append(&self, event: DomainEvent) -> ApplicationResult<()>;
    async fn read_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>>;
    async fn read_team_stats(
        &self,
        match_id: &str,
        team_id: &TeamId,
    ) -> ApplicationResult<Option<TeamStats>>;
    async fn read_player_stats(
        &self,
        match_id: &str,
        player_id: &PlayerId,
    ) -> ApplicationResult<Option<PlayerStats>>;
}
