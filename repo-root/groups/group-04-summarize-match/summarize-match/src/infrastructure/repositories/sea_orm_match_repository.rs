use super::in_memory::replay_events;
use crate::application::{ApplicationError, ApplicationResult, MatchRepository};
use crate::domain::{
    DomainEvent, MatchAggregate, MatchSummary, PlayerId, PlayerStats, TeamId, TeamStats,
};
use async_trait::async_trait;
use sea_orm::{
    ConnectionTrait, DatabaseConnection, DbBackend, QueryResult, Statement, TransactionTrait,
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
        let payload_json = serde_json::to_value(&event)
            .map_err(|e| ApplicationError::repository(e.to_string()))?;

        let txn = self
            .db
            .begin()
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        txn.execute(Statement::from_sql_and_values(
            DbBackend::Postgres,
            "INSERT INTO match_events (match_id, event_type, payload, occurred_at) VALUES ($1, $2, $3, $4)",
            vec![
                match_id.into(),
                event_type.into(),
                payload_json.clone().into(),
                occurred_at.into(),
            ],
        ))
        .await
        .map_err(|error| ApplicationError::repository(error.to_string()))?;

        txn.execute(Statement::from_sql_and_values(
            DbBackend::Postgres,
            "SELECT apply_match_event_stats($1::jsonb)",
            vec![payload_json.into()],
        ))
        .await
        .map_err(|error| ApplicationError::repository(error.to_string()))?;

        txn.commit()
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

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

    async fn read_team_stats(
        &self,
        match_id: &str,
        team_id: &TeamId,
    ) -> ApplicationResult<Option<TeamStats>> {
        let row = self
            .db
            .query_one(Statement::from_sql_and_values(
                DbBackend::Postgres,
                r#"
                SELECT
                    match_id::text AS match_id,
                    team_id::text AS team_id,
                    goals,
                    shots,
                    shots_on_target,
                    passes_attempted,
                    passes_succeeded,
                    saves,
                    fouls_committed,
                    yellow_cards,
                    red_cards,
                    substitutions,
                    players_used
                FROM match_team_stats
                WHERE match_id = $1::uuid AND team_id = $2::uuid
                "#,
                vec![match_id.into(), team_id.0.clone().into()],
            ))
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        row.map(team_stats_from_row).transpose()
    }

    async fn read_player_stats(
        &self,
        match_id: &str,
        player_id: &PlayerId,
    ) -> ApplicationResult<Option<PlayerStats>> {
        let row = self
            .db
            .query_one(Statement::from_sql_and_values(
                DbBackend::Postgres,
                r#"
                SELECT
                    match_id::text AS match_id,
                    player_id::text AS player_id,
                    team_id::text AS team_id,
                    goals,
                    assists,
                    shots,
                    shots_on_target,
                    passes_attempted,
                    passes_succeeded,
                    saves,
                    fouls_committed,
                    yellow_cards,
                    red_cards,
                    substitutions_in,
                    substitutions_out
                FROM match_player_stats
                WHERE match_id = $1::uuid AND player_id = $2::uuid
                "#,
                vec![match_id.into(), player_id.0.clone().into()],
            ))
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        row.map(player_stats_from_row).transpose()
    }
}

fn team_stats_from_row(row: QueryResult) -> ApplicationResult<TeamStats> {
    Ok(TeamStats {
        match_id: row_string(&row, "match_id")?,
        team_id: TeamId(row_string(&row, "team_id")?),
        goals: row_u32(&row, "goals")?,
        shots: row_u32(&row, "shots")?,
        shots_on_target: row_u32(&row, "shots_on_target")?,
        passes_attempted: row_u32(&row, "passes_attempted")?,
        passes_succeeded: row_u32(&row, "passes_succeeded")?,
        saves: row_u32(&row, "saves")?,
        fouls_committed: row_u32(&row, "fouls_committed")?,
        yellow_cards: row_u32(&row, "yellow_cards")?,
        red_cards: row_u32(&row, "red_cards")?,
        substitutions: row_u32(&row, "substitutions")?,
        players_used: row_u32(&row, "players_used")?,
    })
}

fn player_stats_from_row(row: QueryResult) -> ApplicationResult<PlayerStats> {
    Ok(PlayerStats {
        match_id: row_string(&row, "match_id")?,
        player_id: PlayerId(row_string(&row, "player_id")?),
        team_id: TeamId(row_string(&row, "team_id")?),
        goals: row_u32(&row, "goals")?,
        assists: row_u32(&row, "assists")?,
        shots: row_u32(&row, "shots")?,
        shots_on_target: row_u32(&row, "shots_on_target")?,
        passes_attempted: row_u32(&row, "passes_attempted")?,
        passes_succeeded: row_u32(&row, "passes_succeeded")?,
        saves: row_u32(&row, "saves")?,
        fouls_committed: row_u32(&row, "fouls_committed")?,
        yellow_cards: row_u32(&row, "yellow_cards")?,
        red_cards: row_u32(&row, "red_cards")?,
        substitutions_in: row_u32(&row, "substitutions_in")?,
        substitutions_out: row_u32(&row, "substitutions_out")?,
    })
}

fn row_string(row: &QueryResult, column: &str) -> ApplicationResult<String> {
    row.try_get("", column)
        .map_err(|error| ApplicationError::repository(error.to_string()))
}

fn row_u32(row: &QueryResult, column: &str) -> ApplicationResult<u32> {
    let value: i32 = row
        .try_get("", column)
        .map_err(|error| ApplicationError::repository(error.to_string()))?;
    u32::try_from(value).map_err(|error| ApplicationError::repository(error.to_string()))
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
