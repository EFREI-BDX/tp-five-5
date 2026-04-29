use super::in_memory::replay_events;
use crate::application::{ApplicationError, ApplicationResult, MatchRepository};
use crate::domain::{DomainEvent, MatchAggregate, MatchSummary};
use async_trait::async_trait;
use sea_orm::{ConnectionTrait, DatabaseConnection, DbBackend, Statement};

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
        let rows = self
            .db
            .query_all(Statement::from_sql_and_values(
                DbBackend::Postgres,
                "SELECT payload FROM match_events WHERE match_id::text = $1 ORDER BY id",
                vec![match_id.into()],
            ))
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        let events = rows
            .into_iter()
            .map(|row| {
                row.try_get::<serde_json::Value>("", "payload")
                    .map_err(anyhow::Error::from)
                    .and_then(|payload| {
                        serde_json::from_value::<DomainEvent>(payload).map_err(anyhow::Error::from)
                    })
            })
            .collect::<Result<Vec<_>, _>>()
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        replay_events(events)
    }

    async fn append(&self, event: DomainEvent) -> ApplicationResult<()> {
        let match_id = event.match_id().to_string();
        let event_type = event.event_type().to_string();
        let occurred_at = event_occurred_at(&event).to_string();
        let event_id = event_id(&event).to_string();
        let match_time = event_match_time(&event);
        let payload_json = serde_json::to_value(&event)
            .map_err(|e| ApplicationError::repository(e.to_string()))?;

        let values = || {
            vec![
                match_id.clone().into(),
                event_type.clone().into(),
                payload_json.clone().into(),
                occurred_at.clone().into(),
            ]
        };

        let insert_text = Statement::from_sql_and_values(
            DbBackend::Postgres,
            "INSERT INTO match_events (match_id, event_type, payload, occurred_at) VALUES ($1, $2, $3, $4)",
            values(),
        );

        if let Err(first_error) = self.db.execute(insert_text).await {
            if let DomainEvent::MatchStarted(started) = &event {
                let insert_match = Statement::from_sql_and_values(
                    DbBackend::Postgres,
                    "INSERT INTO matches (match_id, home_team_id, away_team_id, scheduled_duration_minutes, occurred_at, computed_home_score, computed_away_score, current_status) VALUES ($1::uuid, $2::uuid, $3::uuid, $4, $5::timestamptz, 0, 0, 'IN_PROGRESS') ON CONFLICT (match_id) DO NOTHING",
                    vec![
                        started.match_id.clone().into(),
                        started.home_team.team_id.0.clone().into(),
                        started.away_team.team_id.0.clone().into(),
                        (started.scheduled_duration_minutes as i16).into(),
                        started.occurred_at.clone().into(),
                    ],
                );

                self.db
                    .execute(insert_match)
                    .await
                    .map_err(|error| ApplicationError::repository(error.to_string()))?;
            }

            let typed_values = vec![
                event_id.into(),
                match_id.into(),
                event_type.into(),
                occurred_at.into(),
                (match_time.minute as i16).into(),
                (match_time.second as i16).into(),
                match_time.period.clone().into(),
                payload_json.into(),
            ];
            let insert_uuid = Statement::from_sql_and_values(
                DbBackend::Postgres,
                "INSERT INTO match_events (event_id, match_id, event_type, occurred_at, time_minute, time_second, time_period, payload) VALUES ($1::uuid, $2::uuid, $3, $4::timestamptz, $5, $6, $7, $8)",
                typed_values,
            );

            self.db.execute(insert_uuid).await.map_err(|second_error| {
                ApplicationError::repository(format!(
                    "{}; fallback with uuid cast failed: {}",
                    first_error, second_error
                ))
            })?;
        }

        Ok(())
    }

    async fn read_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>> {
        let aggregate = self.load(match_id).await?;
        if aggregate.is_known() {
            Ok(Some(aggregate.to_summary(match_id)))
        } else {
            Ok(None)
        }
    }
}

fn event_id(event: &DomainEvent) -> &str {
    match event {
        DomainEvent::MatchStarted(event) => &event.event_id,
        DomainEvent::GoalScored(event) => &event.event_id,
        DomainEvent::GoalCancelled(event) => &event.event_id,
        DomainEvent::MatchFinished(event) => &event.event_id,
        DomainEvent::RedCard(event) => &event.event_id,
        DomainEvent::PassAttempted(event) => &event.event_id,
        DomainEvent::ShotAttempted(event) => &event.event_id,
        DomainEvent::FoulCommitted(event) => &event.event_id,
        DomainEvent::YellowCard(event) => &event.event_id,
        DomainEvent::SaveMade(event) => &event.event_id,
        DomainEvent::Substitution(event) => &event.event_id,
        DomainEvent::MatchPaused(event) => &event.event_id,
        DomainEvent::MatchResumed(event) => &event.event_id,
        DomainEvent::MatchCancelled(event) => &event.event_id,
        DomainEvent::MatchForfeited(event) => &event.event_id,
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

fn event_match_time(event: &DomainEvent) -> &crate::domain::MatchTime {
    match event {
        DomainEvent::MatchStarted(event) => &event.match_time,
        DomainEvent::GoalScored(event) => &event.match_time,
        DomainEvent::GoalCancelled(event) => &event.match_time,
        DomainEvent::MatchFinished(event) => &event.match_time,
        DomainEvent::RedCard(event) => &event.match_time,
        DomainEvent::PassAttempted(event) => &event.match_time,
        DomainEvent::ShotAttempted(event) => &event.match_time,
        DomainEvent::FoulCommitted(event) => &event.match_time,
        DomainEvent::YellowCard(event) => &event.match_time,
        DomainEvent::SaveMade(event) => &event.match_time,
        DomainEvent::Substitution(event) => &event.match_time,
        DomainEvent::MatchPaused(event) => &event.match_time,
        DomainEvent::MatchResumed(event) => &event.match_time,
        DomainEvent::MatchCancelled(event) => &event.match_time,
        DomainEvent::MatchForfeited(event) => &event.match_time,
    }
}
