use super::in_memory::replay_events;
use super::match_event_entity::{self, Entity as MatchEvent};
use crate::application::{ApplicationError, ApplicationResult, MatchRepository};
use crate::domain::{DomainEvent, MatchAggregate};
use async_trait::async_trait;
use sea_orm::{
    ActiveModelTrait, ColumnTrait, DatabaseConnection, EntityTrait, QueryFilter, QueryOrder, Set,
};

#[derive(Clone)]
pub struct SeaOrmMatchRepository {
    db: DatabaseConnection,
}

impl SeaOrmMatchRepository {
    pub fn new(db: DatabaseConnection) -> Self {
        Self { db }
    }
}

#[async_trait]
impl MatchRepository for SeaOrmMatchRepository {
    async fn load(&self, match_id: &str) -> ApplicationResult<MatchAggregate> {
        let rows = MatchEvent::find()
            .filter(match_event_entity::Column::MatchId.eq(match_id))
            .order_by_asc(match_event_entity::Column::Id)
            .all(&self.db)
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        let events = rows
            .into_iter()
            .map(|row| serde_json::from_value::<DomainEvent>(row.payload))
            .collect::<Result<Vec<_>, _>>()
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        replay_events(events)
    }

    async fn append(&self, event: DomainEvent) -> ApplicationResult<()> {
        let model = match_event_entity::ActiveModel {
            match_id: Set(event.match_id().to_string()),
            event_type: Set(event.event_type().to_string()),
            occurred_at: Set(event_occurred_at(&event).to_string()),
            payload: Set(serde_json::to_value(event)
                .map_err(|error| ApplicationError::repository(error.to_string()))?),
            ..Default::default()
        };

        model
            .insert(&self.db)
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;
        Ok(())
    }
}

fn event_occurred_at(event: &DomainEvent) -> &str {
    match event {
        DomainEvent::MatchStarted(event) => &event.occurred_at,
        DomainEvent::GoalScored(event) => &event.occurred_at,
        DomainEvent::GoalCancelled(event) => &event.occurred_at,
        DomainEvent::MatchFinished(event) => &event.occurred_at,
        DomainEvent::RedCard(event) => &event.occurred_at,
        DomainEvent::PassAttempted(event) => &event.occurred_at,
        DomainEvent::ShotAttempted(event) => &event.occurred_at,
        DomainEvent::FoulCommitted(event) => &event.occurred_at,
        DomainEvent::YellowCard(event) => &event.occurred_at,
        DomainEvent::SaveMade(event) => &event.occurred_at,
        DomainEvent::Substitution(event) => &event.occurred_at,
        DomainEvent::MatchPaused(event) => &event.occurred_at,
        DomainEvent::MatchResumed(event) => &event.occurred_at,
        DomainEvent::MatchCancelled(event) => &event.occurred_at,
        DomainEvent::MatchForfeited(event) => &event.occurred_at,
    }
}
