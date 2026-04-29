use anyhow::Result;
use sea_orm::{ConnectionTrait, DatabaseConnection, Statement};

pub struct DatabaseMigrator {
    db: DatabaseConnection,
}

impl DatabaseMigrator {
    pub fn new(db: DatabaseConnection) -> Self {
        Self { db }
    }

    pub async fn run(&self) -> Result<()> {
        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE TABLE IF NOT EXISTS match_events (
                    id SERIAL PRIMARY KEY,
                    match_id TEXT NOT NULL,
                    event_type TEXT NOT NULL,
                    payload JSONB NOT NULL,
                    occurred_at TEXT NOT NULL
                );
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE INDEX IF NOT EXISTS idx_match_events_match_id_id
                    ON match_events (match_id, id);
                "#
                .to_string(),
            ))
            .await?;

        Ok(())
    }
}
