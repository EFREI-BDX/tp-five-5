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
                    event_id UUID NOT NULL,
                    match_id UUID NOT NULL,
                    event_type TEXT NOT NULL,
                    payload JSONB NOT NULL,
                    occurred_at TIMESTAMPTZ NOT NULL,
                    time_minute INTEGER NOT NULL,
                    time_second INTEGER NOT NULL,
                    time_period TEXT NOT NULL,
                    period TEXT NOT NULL
                );
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                ALTER TABLE match_events
                    ADD COLUMN IF NOT EXISTS event_id UUID;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                ALTER TABLE match_events
                    ADD COLUMN IF NOT EXISTS time_minute INTEGER,
                    ADD COLUMN IF NOT EXISTS time_second INTEGER,
                    ADD COLUMN IF NOT EXISTS time_period TEXT,
                    ADD COLUMN IF NOT EXISTS period TEXT;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                UPDATE match_events
                SET event_id = (
                    payload -> (
                        SELECT key
                        FROM jsonb_object_keys(payload) AS event_keys(key)
                        LIMIT 1
                    ) ->> 'event_id'
                )::UUID
                WHERE event_id IS NULL;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                UPDATE match_events
                SET
                    time_minute = (
                        payload -> (
                            SELECT key
                            FROM jsonb_object_keys(payload) AS event_keys(key)
                            LIMIT 1
                        ) #>> '{match_time,minute}'
                    )::INTEGER,
                    time_second = (
                        payload -> (
                            SELECT key
                            FROM jsonb_object_keys(payload) AS event_keys(key)
                            LIMIT 1
                        ) #>> '{match_time,second}'
                    )::INTEGER,
                    time_period = (
                        payload -> (
                            SELECT key
                            FROM jsonb_object_keys(payload) AS event_keys(key)
                            LIMIT 1
                        ) #>> '{match_time,period}'
                    ),
                    period = (
                        payload -> (
                            SELECT key
                            FROM jsonb_object_keys(payload) AS event_keys(key)
                            LIMIT 1
                        ) #>> '{match_time,period}'
                    )
                WHERE
                    time_minute IS NULL
                    OR time_second IS NULL
                    OR time_period IS NULL
                    OR period IS NULL;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                ALTER TABLE match_events
                    ALTER COLUMN time_minute SET NOT NULL,
                    ALTER COLUMN time_second SET NOT NULL,
                    ALTER COLUMN time_period SET NOT NULL,
                    ALTER COLUMN period SET NOT NULL;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                ALTER TABLE match_events
                    ALTER COLUMN event_id SET NOT NULL;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                ALTER TABLE match_events
                    ALTER COLUMN match_id TYPE UUID USING match_id::UUID;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                ALTER TABLE match_events
                    ALTER COLUMN occurred_at TYPE TIMESTAMPTZ USING occurred_at::TIMESTAMPTZ;
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

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE TABLE IF NOT EXISTS match_team_stats (
                    match_id UUID NOT NULL,
                    team_id UUID NOT NULL,
                    goals INTEGER NOT NULL DEFAULT 0,
                    shots INTEGER NOT NULL DEFAULT 0,
                    shots_on_target INTEGER NOT NULL DEFAULT 0,
                    passes_attempted INTEGER NOT NULL DEFAULT 0,
                    passes_succeeded INTEGER NOT NULL DEFAULT 0,
                    saves INTEGER NOT NULL DEFAULT 0,
                    fouls_committed INTEGER NOT NULL DEFAULT 0,
                    yellow_cards INTEGER NOT NULL DEFAULT 0,
                    red_cards INTEGER NOT NULL DEFAULT 0,
                    substitutions INTEGER NOT NULL DEFAULT 0,
                    players_used INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY (match_id, team_id)
                );
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE TABLE IF NOT EXISTS match_player_stats (
                    match_id UUID NOT NULL,
                    player_id UUID NOT NULL,
                    team_id UUID NOT NULL,
                    goals INTEGER NOT NULL DEFAULT 0,
                    assists INTEGER NOT NULL DEFAULT 0,
                    shots INTEGER NOT NULL DEFAULT 0,
                    shots_on_target INTEGER NOT NULL DEFAULT 0,
                    passes_attempted INTEGER NOT NULL DEFAULT 0,
                    passes_succeeded INTEGER NOT NULL DEFAULT 0,
                    saves INTEGER NOT NULL DEFAULT 0,
                    fouls_committed INTEGER NOT NULL DEFAULT 0,
                    yellow_cards INTEGER NOT NULL DEFAULT 0,
                    red_cards INTEGER NOT NULL DEFAULT 0,
                    substitutions_in INTEGER NOT NULL DEFAULT 0,
                    substitutions_out INTEGER NOT NULL DEFAULT 0,
                    PRIMARY KEY (match_id, player_id)
                );
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE TABLE IF NOT EXISTS match_goal_index (
                    match_id UUID NOT NULL,
                    event_id UUID NOT NULL,
                    scoring_team_id UUID NOT NULL,
                    scorer_id UUID NOT NULL,
                    assist_id UUID NULL,
                    is_own_goal BOOLEAN NOT NULL DEFAULT FALSE,
                    cancelled BOOLEAN NOT NULL DEFAULT FALSE,
                    PRIMARY KEY (match_id, event_id)
                );
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE TABLE IF NOT EXISTS match_player_registry (
                    match_id UUID NOT NULL,
                    player_id UUID NOT NULL,
                    team_id UUID NOT NULL,
                    PRIMARY KEY (match_id, player_id)
                );
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE OR REPLACE FUNCTION apply_match_event_stats(event_json JSONB)
                RETURNS VOID AS $$
                DECLARE
                    event_type TEXT;
                    event_body JSONB;
                    player JSONB;
                    match_uuid UUID;
                    event_uuid UUID;
                    team_uuid UUID;
                    player_uuid UUID;
                    assist_uuid UUID;
                    goal_row match_goal_index%ROWTYPE;
                BEGIN
                    SELECT value INTO event_type FROM jsonb_object_keys(event_json) AS keys(value) LIMIT 1;
                    IF event_type IS NULL THEN
                        RAISE EXCEPTION 'Invalid domain event JSON: missing variant';
                    END IF;

                    event_body := event_json -> event_type;
                    match_uuid := (event_body ->> 'match_id')::UUID;

                    IF event_type = 'MatchStarted' THEN
                        team_uuid := (event_body #>> '{home_team,teamId}')::UUID;
                        INSERT INTO match_team_stats (match_id, team_id)
                        VALUES (match_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;

                        FOR player IN SELECT value FROM jsonb_array_elements(event_body #> '{home_team,startingPlayers}')
                        LOOP
                            player_uuid := (player ->> 'playerId')::UUID;
                            INSERT INTO match_player_registry (match_id, player_id, team_id)
                            VALUES (match_uuid, player_uuid, team_uuid)
                            ON CONFLICT DO NOTHING;
                            INSERT INTO match_player_stats (match_id, player_id, team_id)
                            VALUES (match_uuid, player_uuid, team_uuid)
                            ON CONFLICT DO NOTHING;
                        END LOOP;

                        UPDATE match_team_stats
                        SET players_used = (
                            SELECT COUNT(*)::INTEGER FROM match_player_registry
                            WHERE match_id = match_uuid AND team_id = team_uuid
                        )
                        WHERE match_id = match_uuid AND team_id = team_uuid;

                        team_uuid := (event_body #>> '{away_team,teamId}')::UUID;
                        INSERT INTO match_team_stats (match_id, team_id)
                        VALUES (match_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;

                        FOR player IN SELECT value FROM jsonb_array_elements(event_body #> '{away_team,startingPlayers}')
                        LOOP
                            player_uuid := (player ->> 'playerId')::UUID;
                            INSERT INTO match_player_registry (match_id, player_id, team_id)
                            VALUES (match_uuid, player_uuid, team_uuid)
                            ON CONFLICT DO NOTHING;
                            INSERT INTO match_player_stats (match_id, player_id, team_id)
                            VALUES (match_uuid, player_uuid, team_uuid)
                            ON CONFLICT DO NOTHING;
                        END LOOP;

                        UPDATE match_team_stats
                        SET players_used = (
                            SELECT COUNT(*)::INTEGER FROM match_player_registry
                            WHERE match_id = match_uuid AND team_id = team_uuid
                        )
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                    ELSIF event_type = 'GoalScored' THEN
                        event_uuid := (event_body ->> 'event_id')::UUID;
                        team_uuid := (event_body ->> 'scoring_team_id')::UUID;
                        player_uuid := (event_body ->> 'scorer_id')::UUID;
                        assist_uuid := NULLIF(event_body ->> 'assist_id', 'null')::UUID;

                        INSERT INTO match_team_stats (match_id, team_id)
                        VALUES (match_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET goals = goals + 1
                        WHERE match_id = match_uuid AND team_id = team_uuid;

                        INSERT INTO match_goal_index (
                            match_id, event_id, scoring_team_id, scorer_id, assist_id, is_own_goal
                        )
                        VALUES (
                            match_uuid,
                            event_uuid,
                            team_uuid,
                            player_uuid,
                            assist_uuid,
                            COALESCE((event_body ->> 'is_own_goal')::BOOLEAN, FALSE)
                        )
                        ON CONFLICT DO NOTHING;

                        IF COALESCE((event_body ->> 'is_own_goal')::BOOLEAN, FALSE) = FALSE THEN
                            INSERT INTO match_player_stats (match_id, player_id, team_id)
                            VALUES (match_uuid, player_uuid, team_uuid)
                            ON CONFLICT DO NOTHING;
                            UPDATE match_player_stats
                            SET goals = goals + 1
                            WHERE match_id = match_uuid AND player_id = player_uuid;
                        END IF;

                        IF assist_uuid IS NOT NULL THEN
                            INSERT INTO match_player_stats (match_id, player_id, team_id)
                            VALUES (match_uuid, assist_uuid, team_uuid)
                            ON CONFLICT DO NOTHING;
                            UPDATE match_player_stats
                            SET assists = assists + 1
                            WHERE match_id = match_uuid AND player_id = assist_uuid;
                        END IF;
                    ELSIF event_type = 'GoalCancelled' THEN
                        event_uuid := (event_body ->> 'cancelled_goal_event_id')::UUID;

                        SELECT * INTO goal_row
                        FROM match_goal_index
                        WHERE match_id = match_uuid AND event_id = event_uuid AND cancelled = FALSE;

                        IF NOT FOUND THEN
                            RAISE EXCEPTION 'GOAL_CANCELLED references an unknown or already cancelled goal: %', event_uuid;
                        END IF;

                        UPDATE match_goal_index
                        SET cancelled = TRUE
                        WHERE match_id = match_uuid AND event_id = event_uuid;

                        UPDATE match_team_stats
                        SET goals = GREATEST(goals - 1, 0)
                        WHERE match_id = match_uuid AND team_id = goal_row.scoring_team_id;

                        IF goal_row.is_own_goal = FALSE THEN
                            UPDATE match_player_stats
                            SET goals = GREATEST(goals - 1, 0)
                            WHERE match_id = match_uuid AND player_id = goal_row.scorer_id;
                        END IF;

                        IF goal_row.assist_id IS NOT NULL THEN
                            UPDATE match_player_stats
                            SET assists = GREATEST(assists - 1, 0)
                            WHERE match_id = match_uuid AND player_id = goal_row.assist_id;
                        END IF;
                    ELSIF event_type = 'PassAttempted' THEN
                        team_uuid := (event_body ->> 'team_id')::UUID;
                        player_uuid := (event_body ->> 'player_id')::UUID;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET passes_attempted = passes_attempted + 1,
                            passes_succeeded = passes_succeeded + CASE WHEN (event_body ->> 'succeeded')::BOOLEAN THEN 1 ELSE 0 END
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                        UPDATE match_player_stats
                        SET passes_attempted = passes_attempted + 1,
                            passes_succeeded = passes_succeeded + CASE WHEN (event_body ->> 'succeeded')::BOOLEAN THEN 1 ELSE 0 END
                        WHERE match_id = match_uuid AND player_id = player_uuid;
                    ELSIF event_type = 'ShotAttempted' THEN
                        team_uuid := (event_body ->> 'team_id')::UUID;
                        player_uuid := (event_body ->> 'player_id')::UUID;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET shots = shots + 1,
                            shots_on_target = shots_on_target + CASE WHEN (event_body ->> 'on_target')::BOOLEAN THEN 1 ELSE 0 END
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                        UPDATE match_player_stats
                        SET shots = shots + 1,
                            shots_on_target = shots_on_target + CASE WHEN (event_body ->> 'on_target')::BOOLEAN THEN 1 ELSE 0 END
                        WHERE match_id = match_uuid AND player_id = player_uuid;
                    ELSIF event_type = 'SaveMade' THEN
                        team_uuid := (event_body ->> 'keeper_team_id')::UUID;
                        player_uuid := (event_body ->> 'keeper_id')::UUID;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET saves = saves + 1
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                        UPDATE match_player_stats
                        SET saves = saves + 1
                        WHERE match_id = match_uuid AND player_id = player_uuid;
                    ELSIF event_type = 'FoulCommitted' THEN
                        team_uuid := (event_body ->> 'team_id')::UUID;
                        player_uuid := (event_body ->> 'player_id')::UUID;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET fouls_committed = fouls_committed + 1
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                        UPDATE match_player_stats
                        SET fouls_committed = fouls_committed + 1
                        WHERE match_id = match_uuid AND player_id = player_uuid;
                    ELSIF event_type = 'YellowCard' THEN
                        team_uuid := (event_body ->> 'team_id')::UUID;
                        player_uuid := (event_body ->> 'player_id')::UUID;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET yellow_cards = yellow_cards + 1
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                        UPDATE match_player_stats
                        SET yellow_cards = yellow_cards + 1
                        WHERE match_id = match_uuid AND player_id = player_uuid;
                    ELSIF event_type = 'RedCard' THEN
                        team_uuid := (event_body ->> 'team_id')::UUID;
                        player_uuid := (event_body ->> 'player_id')::UUID;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET red_cards = red_cards + 1
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                        UPDATE match_player_stats
                        SET red_cards = red_cards + 1
                        WHERE match_id = match_uuid AND player_id = player_uuid;
                    ELSIF event_type = 'Substitution' THEN
                        team_uuid := (event_body ->> 'team_id')::UUID;
                        INSERT INTO match_team_stats (match_id, team_id)
                        VALUES (match_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_team_stats
                        SET substitutions = substitutions + 1
                        WHERE match_id = match_uuid AND team_id = team_uuid;

                        player_uuid := (event_body ->> 'player_out')::UUID;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_player_stats
                        SET substitutions_out = substitutions_out + 1
                        WHERE match_id = match_uuid AND player_id = player_uuid;

                        player_uuid := (event_body ->> 'player_in')::UUID;
                        INSERT INTO match_player_registry (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        INSERT INTO match_player_stats (match_id, player_id, team_id)
                        VALUES (match_uuid, player_uuid, team_uuid)
                        ON CONFLICT DO NOTHING;
                        UPDATE match_player_stats
                        SET substitutions_in = substitutions_in + 1
                        WHERE match_id = match_uuid AND player_id = player_uuid;
                        UPDATE match_team_stats
                        SET players_used = (
                            SELECT COUNT(*)::INTEGER FROM match_player_registry
                            WHERE match_id = match_uuid AND team_id = team_uuid
                        )
                        WHERE match_id = match_uuid AND team_id = team_uuid;
                    END IF;
                END;
                $$ LANGUAGE plpgsql;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE OR REPLACE FUNCTION get_match_team_stats(
                    p_match_id UUID,
                    p_team_id UUID
                )
                RETURNS TABLE (
                    match_id UUID,
                    team_id UUID,
                    goals INTEGER,
                    shots INTEGER,
                    shots_on_target INTEGER,
                    passes_attempted INTEGER,
                    passes_succeeded INTEGER,
                    saves INTEGER,
                    fouls_committed INTEGER,
                    yellow_cards INTEGER,
                    red_cards INTEGER,
                    substitutions INTEGER,
                    players_used INTEGER
                ) AS $$
                    SELECT
                        stats.match_id,
                        stats.team_id,
                        stats.goals,
                        stats.shots,
                        stats.shots_on_target,
                        stats.passes_attempted,
                        stats.passes_succeeded,
                        stats.saves,
                        stats.fouls_committed,
                        stats.yellow_cards,
                        stats.red_cards,
                        stats.substitutions,
                        stats.players_used
                    FROM match_team_stats stats
                    WHERE stats.match_id = p_match_id AND stats.team_id = p_team_id;
                $$ LANGUAGE sql STABLE;
                "#
                .to_string(),
            ))
            .await?;

        self.db
            .execute(Statement::from_string(
                self.db.get_database_backend(),
                r#"
                CREATE OR REPLACE FUNCTION get_match_player_stats(
                    p_match_id UUID,
                    p_player_id UUID
                )
                RETURNS TABLE (
                    match_id UUID,
                    player_id UUID,
                    team_id UUID,
                    goals INTEGER,
                    assists INTEGER,
                    shots INTEGER,
                    shots_on_target INTEGER,
                    passes_attempted INTEGER,
                    passes_succeeded INTEGER,
                    saves INTEGER,
                    fouls_committed INTEGER,
                    yellow_cards INTEGER,
                    red_cards INTEGER,
                    substitutions_in INTEGER,
                    substitutions_out INTEGER
                ) AS $$
                    SELECT
                        stats.match_id,
                        stats.player_id,
                        stats.team_id,
                        stats.goals,
                        stats.assists,
                        stats.shots,
                        stats.shots_on_target,
                        stats.passes_attempted,
                        stats.passes_succeeded,
                        stats.saves,
                        stats.fouls_committed,
                        stats.yellow_cards,
                        stats.red_cards,
                        stats.substitutions_in,
                        stats.substitutions_out
                    FROM match_player_stats stats
                    WHERE stats.match_id = p_match_id AND stats.player_id = p_player_id;
                $$ LANGUAGE sql STABLE;
                "#
                .to_string(),
            ))
            .await?;

        Ok(())
    }
}
