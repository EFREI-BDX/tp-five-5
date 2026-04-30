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

CREATE OR REPLACE FUNCTION fn_insert_match(
    p_uuid UUID,
    p_team_a_uuid UUID,
    p_team_b_uuid UUID,
    p_field_uuid UUID,
    p_start TIMESTAMP,
    p_end TIMESTAMP,
    p_status_code VARCHAR
) RETURNS UUID
    LANGUAGE plpgsql AS
$$
DECLARE
    v_team_a_id BIGINT;
    v_team_b_id BIGINT;
    v_field_id  BIGINT;
    v_status_id BIGINT;
    v_match_id  BIGINT;
BEGIN
    SELECT id INTO v_team_a_id FROM teams WHERE uuid = p_team_a_uuid;
    IF v_team_a_id IS NULL THEN
        RAISE EXCEPTION 'Team A introuvable (UUID: %)', p_team_a_uuid;
    END IF;

    SELECT id INTO v_team_b_id FROM teams WHERE uuid = p_team_b_uuid;
    IF v_team_b_id IS NULL THEN
        RAISE EXCEPTION 'Team B introuvable (UUID: %)', p_team_b_uuid;
    END IF;

    SELECT id INTO v_field_id FROM fields WHERE uuid = p_field_uuid;
    IF v_field_id IS NULL THEN
        RAISE EXCEPTION 'Field introuvable (UUID: %)', p_field_uuid;
    END IF;

    SELECT id INTO v_status_id FROM ref_status WHERE code = p_status_code;
    IF v_status_id IS NULL THEN
        RAISE EXCEPTION 'Status code "%" inexistant dans ref_status', p_status_code;
    END IF;

    IF p_team_a_uuid = p_team_b_uuid THEN
        RAISE EXCEPTION 'Un match ne peut pas opposer la même équipe (UUID: %)', p_team_a_uuid;
    END IF;

    INSERT INTO matches (uuid, team_a_id, team_b_id, field_id, start_time, end_time, current_status)
    VALUES (p_uuid, v_team_a_id, v_team_b_id, v_field_id, p_start, p_end, v_status_id)

    RETURNING id INTO v_match_id;

    INSERT INTO match_status_history (match_id, status)
    VALUES (v_match_id, v_status_id);

    RETURN p_uuid;
END;
$$;

CREATE OR REPLACE FUNCTION fn_update_match_status(p_match_uuid UUID, p_new_status_code VARCHAR)
    RETURNS BOOLEAN
    LANGUAGE plpgsql AS
$$
DECLARE
    v_status_id BIGINT;
    v_match_id  BIGINT;
    v_current_status_id BIGINT;
BEGIN
    SELECT id INTO v_status_id FROM ref_status WHERE code = p_new_status_code;
    IF v_status_id IS NULL THEN RAISE EXCEPTION 'Statut % inconnu', p_new_status_code; END IF;

    SELECT id, current_status INTO v_match_id, v_current_status_id
    FROM matches WHERE uuid = p_match_uuid;

    IF v_match_id IS NULL THEN
        RAISE EXCEPTION 'Match % non trouvé', p_match_uuid;
    END IF;

    IF v_current_status_id = v_status_id THEN
        RETURN FALSE;
    END IF;

    UPDATE matches
    SET current_status = v_status_id
    WHERE id = v_match_id;

    INSERT INTO match_status_history (match_id, status)
    VALUES (v_match_id, v_status_id);

    RETURN TRUE;
END;
$$;

CREATE
    OR REPLACE FUNCTION fn_get_match_details(p_match_uuid UUID)
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

CREATE OR REPLACE FUNCTION fn_upsert_team(
    p_uuid UUID,
    p_tag VARCHAR,
    p_label VARCHAR
) RETURNS UUID
    LANGUAGE plpgsql AS
$$
BEGIN
    INSERT INTO teams (uuid, tag, label)
    VALUES (p_uuid, p_tag, p_label)
    ON CONFLICT (uuid) DO UPDATE
        SET tag = EXCLUDED.tag,
            label = EXCLUDED.label;

    RETURN p_uuid;
END;
$$;

CREATE OR REPLACE FUNCTION fn_upsert_field(
    p_uuid UUID,
    p_label VARCHAR
) RETURNS UUID
    LANGUAGE plpgsql AS
$$
BEGIN
    INSERT INTO fields (uuid, label)
    VALUES (p_uuid, p_label)
    ON CONFLICT (uuid) DO UPDATE
        SET label = EXCLUDED.label;

    RETURN p_uuid;
END;
$$;

CREATE OR REPLACE FUNCTION fn_get_team_by_uuid(p_team_uuid UUID)
    RETURNS TABLE
            (
                uuid  UUID,
                tag   VARCHAR,
                label VARCHAR
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT t.uuid, t.tag, t.label
        FROM teams t
        WHERE t.uuid = p_team_uuid;
END;
$$;


CREATE OR REPLACE FUNCTION fn_get_field_by_uuid(p_field_uuid UUID)
    RETURNS TABLE
            (
                uuid  UUID,
                label VARCHAR
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY
        SELECT f.uuid, f.label
        FROM fields f
        WHERE f.uuid = p_field_uuid;
END;
$$;