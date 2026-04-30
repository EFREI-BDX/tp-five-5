use super::in_memory::replay_events;
use crate::application::{
    ApplicationError, ApplicationResult, MatchRepository, MatchStatsRepository,
};
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
                "SELECT payload FROM match_events WHERE match_id = $1::uuid ORDER BY id",
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
        let event_id = event.event_id().to_string();
        let match_id = event.match_id().to_string();
        let event_type = event.event_type().to_string();
        let occurred_at = event.occurred_at().to_string();
        let match_time = event.match_time();
        let payload_json = serde_json::to_value(&event)
            .map_err(|e| ApplicationError::repository(e.to_string()))?;

        let txn = self
            .db
            .begin()
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        if let DomainEvent::MatchStarted(match_started) = &event {
            txn.execute(Statement::from_sql_and_values(
                DbBackend::Postgres,
                r#"
                INSERT INTO matches (
                    match_id,
                    home_team_id,
                    away_team_id,
                    scheduled_duration_minutes,
                    occurred_at
                )
                VALUES ($1::uuid, $2::uuid, $3::uuid, $4, $5::timestamptz)
                ON CONFLICT (match_id) DO NOTHING
                "#,
                vec![
                    match_id.clone().into(),
                    match_started.home_team.team_id.0.clone().into(),
                    match_started.away_team.team_id.0.clone().into(),
                    (match_started.scheduled_duration_minutes as i32).into(),
                    occurred_at.clone().into(),
                ],
            ))
            .await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;
        }

        txn.execute(Statement::from_sql_and_values(
            DbBackend::Postgres,
            "INSERT INTO match_events (event_id, match_id, event_type, payload, occurred_at, time_minute, time_second, time_period, period) VALUES ($1::uuid, $2::uuid, $3, $4, $5::timestamptz, $6, $7, $8, $9)",
            vec![
                event_id.into(),
                match_id.into(),
                event_type.into(),
                payload_json.clone().into(),
                occurred_at.into(),
                (match_time.minute as i32).into(),
                (match_time.second as i32).into(),
                match_time.period.clone().into(),
                match_time.period.clone().into(),
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
}

#[async_trait]
impl MatchStatsRepository for SeaOrmMatchRepository {
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
                FROM get_match_team_stats($1::uuid, $2::uuid)
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
                FROM get_match_player_stats($1::uuid, $2::uuid)
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
