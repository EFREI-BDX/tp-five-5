DROP PROCEDURE IF EXISTS sp_insert_match CASCADE;
DROP PROCEDURE IF EXISTS sp_update_match_status CASCADE;
DROP FUNCTION IF EXISTS fn_get_match_details CASCADE;
DROP TABLE IF EXISTS match_status_history CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS fields CASCADE;
DROP TABLE IF EXISTS ref_status CASCADE;

CREATE TABLE ref_status
(
    id   BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO ref_status (code)
VALUES ('NOT_STARTED'),
       ('IN_PROGRESS'),
       ('FINISHED'),
       ('CANCELLED');

CREATE TABLE fields
(
    id    BIGSERIAL PRIMARY KEY,
    uuid  UUID UNIQUE  NOT NULL,
    label VARCHAR(255) NOT NULL
);

CREATE TABLE teams
(
    id    BIGSERIAL PRIMARY KEY,
    uuid  UUID UNIQUE  NOT NULL,
    tag   VARCHAR(10)  NOT NULL,
    label VARCHAR(255) NOT NULL
);

CREATE TABLE matches
(
    id             BIGSERIAL PRIMARY KEY,
    uuid           UUID UNIQUE NOT NULL,
    team_a_id      BIGINT      NOT NULL REFERENCES teams (id),
    team_b_id      BIGINT      NOT NULL REFERENCES teams (id),
    field_id       BIGINT      NOT NULL REFERENCES fields (id),
    start_time     TIMESTAMP   NOT NULL,
    end_time       TIMESTAMP   NOT NULL,
    current_status BIGINT      NOT NULL REFERENCES ref_status (id),
    CONSTRAINT chk_dates CHECK (end_time > start_time),
    CONSTRAINT chk_different_teams CHECK (team_a_id <> team_b_id)
);

CREATE TABLE match_status_history
(
    id         BIGSERIAL PRIMARY KEY,
    match_id   BIGINT    NOT NULL REFERENCES matches (id) ON DELETE CASCADE,
    status     BIGINT    NOT NULL REFERENCES ref_status (id),
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE PROCEDURE sp_insert_match(
    p_uuid UUID,
    p_team_a_uuid UUID,
    p_team_b_uuid UUID,
    p_field_uuid UUID,
    p_start TIMESTAMP,
    p_end TIMESTAMP,
    p_status_code VARCHAR,
    p_team_a_tag VARCHAR,
    p_team_a_label VARCHAR,
    p_team_b_tag VARCHAR,
    p_team_b_label VARCHAR,
    p_field_label VARCHAR
)
    LANGUAGE plpgsql AS
$$
DECLARE
    v_team_a_id BIGINT;
    v_team_b_id BIGINT;
    v_field_id  BIGINT;
    v_status_id BIGINT;
    v_match_id  BIGINT;
BEGIN
    INSERT INTO teams (uuid, tag, label)
    VALUES (p_team_a_uuid, p_team_a_tag, p_team_a_label)
    ON CONFLICT (uuid) DO UPDATE SET tag   = EXCLUDED.tag,
                                     label = EXCLUDED.label
    RETURNING id INTO v_team_a_id;

    INSERT INTO teams (uuid, tag, label)
    VALUES (p_team_b_uuid, p_team_b_tag, p_team_b_label)
    ON CONFLICT (uuid) DO UPDATE SET tag   = EXCLUDED.tag,
                                     label = EXCLUDED.label
    RETURNING id INTO v_team_b_id;

    INSERT INTO fields (uuid, label)
    VALUES (p_field_uuid, p_field_label)
    ON CONFLICT (uuid) DO UPDATE SET label = EXCLUDED.label
    RETURNING id INTO v_field_id;

    SELECT id INTO v_status_id FROM ref_status WHERE code = p_status_code;

    IF v_status_id IS NULL THEN
        RAISE EXCEPTION 'Statut code % non trouvé dans ref_status', p_status_code;
    END IF;

    INSERT INTO matches (uuid, team_a_id, team_b_id, field_id, start_time, end_time, current_status)
    VALUES (p_uuid, v_team_a_id, v_team_b_id, v_field_id, p_start, p_end, v_status_id)
    ON CONFLICT (uuid) DO UPDATE SET start_time     = EXCLUDED.start_time,
                                     end_time       = EXCLUDED.end_time,
                                     current_status = EXCLUDED.current_status
    RETURNING id INTO v_match_id;

    INSERT INTO match_status_history (match_id, status)
    VALUES (v_match_id, v_status_id);
END;
$$;

CREATE OR REPLACE PROCEDURE sp_update_match_status(p_match_uuid UUID, p_new_status_code VARCHAR)
    LANGUAGE plpgsql AS
$$
DECLARE
    v_status_id BIGINT;
    v_match_id  BIGINT;
BEGIN
    SELECT id INTO v_status_id FROM ref_status WHERE code = p_new_status_code;

    UPDATE matches
    SET current_status = v_status_id
    WHERE uuid = p_match_uuid
    RETURNING id INTO v_match_id;

    INSERT INTO match_status_history (match_id, status)
    VALUES (v_match_id, v_status_id);
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_match_details(p_match_uuid UUID)
    RETURNS TABLE
            (
                match_uuid   UUID,
                start_time   TIMESTAMP,
                end_time     TIMESTAMP,
                status       VARCHAR,
                field_uuid   UUID,
                field_label  VARCHAR,
                team_a_uuid  UUID,
                team_a_tag   VARCHAR,
                team_a_label VARCHAR,
                team_b_uuid  UUID,
                team_b_tag   VARCHAR,
                team_b_label VARCHAR
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT m.uuid,
               m.start_time,
               m.end_time,
               rs.code,
               f.uuid,
               f.label,
               ta.uuid,
               ta.tag,
               ta.label,
               tb.uuid,
               tb.tag,
               tb.label
        FROM matches m
                 JOIN ref_status rs ON m.current_status = rs.id
                 JOIN fields f ON m.field_id = f.id
                 JOIN teams ta ON m.team_a_id = ta.id
                 JOIN teams tb ON m.team_b_id = tb.id
        WHERE m.uuid = p_match_uuid;
END;
$$;