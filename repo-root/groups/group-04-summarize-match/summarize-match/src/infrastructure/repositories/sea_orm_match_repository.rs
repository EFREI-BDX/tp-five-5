use super::in_memory::replay_events;
use super::match_event_entity::{self, Entity as MatchEvent};
use crate::application::{ApplicationError, ApplicationResult, MatchRepository};
use crate::domain::{DomainEvent, MatchAggregate, MatchSummary};
use async_trait::async_trait;
use sea_orm::{
    ActiveModelTrait, ColumnTrait, DatabaseConnection, EntityTrait, QueryFilter, QueryOrder, Set,
    ConnectionTrait, Statement, DbBackend
};
use sea_orm::sea_query::Value;

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
        let match_id_val = uuid::Uuid::parse_str(event.match_id())
            .map_err(|e| ApplicationError::repository(format!("Invalid UUID: {}", e)))?;

        let payload_json = serde_json::to_value(&event)
            .map_err(|e| ApplicationError::repository(e.to_string()))?;

        // Format mapping custom vers SQL pur
        let stmt = match event {
            DomainEvent::MatchStarted(ref e) => {
                let event_id = uuid::Uuid::parse_str(&e.event_id).map_err(|e| ApplicationError::repository(e.to_string()))?;
                let home_team_id = uuid::Uuid::parse_str(&e.home_team.team_id.0).map_err(|e| ApplicationError::repository(e.to_string()))?;
                let away_team_id = uuid::Uuid::parse_str(&e.away_team.team_id.0).map_err(|e| ApplicationError::repository(e.to_string()))?;

                let home_roster = serde_json::to_value(&e.home_team.starting_players).unwrap();
                let away_roster = serde_json::to_value(&e.away_team.starting_players).unwrap();

                Statement::from_sql_and_values(
                    DbBackend::Postgres,
                    "CALL cmd_start_match($1, $2, $3, ROW($4, $5, $6), $7, $8, $9, $10, $11, $12)",
                    vec![
                        event_id.into(),
                        match_id_val.into(),
                        e.occurred_at.clone().into(),
                        (e.match_time.minute as i16).into(),
                        (e.match_time.second as i16).into(),
                        e.match_time.period.clone().into(),
                        (e.scheduled_duration_minutes as i16).into(),
                        home_team_id.into(),
                        away_team_id.into(),
                        home_roster.into(),
                        away_roster.into(),
                        payload_json.into(),
                    ],
                )
            }
            DomainEvent::GoalScored(ref e) => {
                let event_id = uuid::Uuid::parse_str(&e.event_id).map_err(|e| ApplicationError::repository(e.to_string()))?;
                let scoring_team_id = uuid::Uuid::parse_str(&e.scoring_team_id.0).map_err(|e| ApplicationError::repository(e.to_string()))?;
                let scorer_id = uuid::Uuid::parse_str(&e.scorer_id.0).map_err(|e| ApplicationError::repository(e.to_string()))?;
                let assist_id = e.assist_id.as_ref().map(|id| uuid::Uuid::parse_str(&id.0)).transpose().unwrap_or(None);

                Statement::from_sql_and_values(
                    DbBackend::Postgres,
                    "CALL cmd_score_goal($1, $2, $3, ROW($4, $5, $6), $7, $8, $9, $10, $11)",
                    vec![
                        event_id.into(),
                        match_id_val.into(),
                        e.occurred_at.clone().into(),
                        (e.match_time.minute as i16).into(),
                        (e.match_time.second as i16).into(),
                        e.match_time.period.clone().into(),
                        scoring_team_id.into(),
                        scorer_id.into(),
                        assist_id.into(),
                        e.is_own_goal.into(),
                        payload_json.into(),
                    ],
                )
            }
            DomainEvent::MatchFinished(ref e) => {
                let event_id = uuid::Uuid::parse_str(&e.event_id).map_err(|e| ApplicationError::repository(e.to_string()))?;
                Statement::from_sql_and_values(
                    DbBackend::Postgres,
                    "CALL cmd_finish_match($1, $2, $3, ROW($4, $5, $6), $7)",
                    vec![
                        event_id.into(),
                        match_id_val.into(),
                        e.occurred_at.clone().into(),
                        (e.match_time.minute as i16).into(),
                        (e.match_time.second as i16).into(),
                        e.match_time.period.clone().into(),
                        payload_json.into(),
                    ]
                )
            }
            // Exemple : Délégation générique pour tous les autres Event DDD
            generic_event => {
                let occurred_at = event_occurred_at(&generic_event).to_string();
                let event_id = uuid::Uuid::new_v4(); // Idéalement, devrait être extrait depuis le DTO event
                let time_min: i16 = 0;
                let time_sec: i16 = 0;
                let time_per = "FIRST_HALF".to_string();

                Statement::from_sql_and_values(
                    DbBackend::Postgres,
                    "CALL cmd_append_generic_event($1, $2, $3, $4, ROW($5, $6, $7), $8)",
                    vec![
                        event_id.into(),
                        match_id_val.into(),
                        generic_event.event_type().into(),
                        occurred_at.into(),
                        time_min.into(),
                        time_sec.into(),
                        time_per.into(),
                        payload_json.into(),
                    ],
                )
            }
        };

        // Exécution de la commande brute via l'interface SeaORM
        self.db.execute(stmt).await
            .map_err(|error| ApplicationError::repository(format!("Stored procedure failed: {}", error)))?;

        Ok(())
    }

    async fn read_summary(&self, match_id: &str) -> ApplicationResult<Option<MatchSummary>> {
        let match_id_val = uuid::Uuid::parse_str(match_id)
            .map_err(|e| ApplicationError::repository(format!("Invalid UUID: {}", e)))?;

        let stmt = Statement::from_sql_and_values(
            DbBackend::Postgres,
            "SELECT summary_json FROM vw_match_summary WHERE match_id = $1",
            vec![match_id_val.into()]
        );

        let result = self.db.query_one(stmt).await
            .map_err(|error| ApplicationError::repository(error.to_string()))?;

        match result {
            Some(row) => {
                let json_val: serde_json::Value = row.try_get("", "summary_json")
                    .map_err(|e| ApplicationError::repository(e.to_string()))?;

                let summary: MatchSummary = serde_json::from_value(json_val)
                    .map_err(|e| ApplicationError::repository(e.to_string()))?;

                Ok(Some(summary))
            }
            None => Ok(None)
        }
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
