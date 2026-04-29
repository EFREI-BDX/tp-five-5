mod error;
mod event_publisher;
mod player_data_port;
mod query_service;
mod repository;
mod service;

use crate::domain::DomainEvent;
use async_trait::async_trait;

pub use error::{ApplicationError, ApplicationResult};
pub use event_publisher::{DomainEventPublisher, NoOpPublisher};
pub use player_data_port::{NoOpPlayerDataPort, PlayerDataPort};
pub use query_service::{MatchQueryService, MatchReadService};
pub use repository::MatchRepository;
pub use service::MatchSummaryService;

#[async_trait]
pub trait ApplicationService: Send + Sync + 'static {
    async fn handle_event(&self, event: DomainEvent) -> ApplicationResult<()>;
}
