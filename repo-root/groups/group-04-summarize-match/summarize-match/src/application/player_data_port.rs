use super::ApplicationResult;
use crate::domain::PlayerData;
use async_trait::async_trait;

#[async_trait]
pub trait PlayerDataPort: Send + Sync + 'static {
    async fn publish(&self, events: &[PlayerData]) -> ApplicationResult<()>;
}

pub struct NoOpPlayerDataPort;

#[async_trait]
impl PlayerDataPort for NoOpPlayerDataPort {
    async fn publish(&self, _events: &[PlayerData]) -> ApplicationResult<()> {
        Ok(())
    }
}
