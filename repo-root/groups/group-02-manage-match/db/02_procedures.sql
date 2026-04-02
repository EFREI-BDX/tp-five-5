-- =============================================================================
-- group-02-manage-match | Stored Procedures (CRUD + Business Rules)
-- Dialect : PostgreSQL / PL/pgSQL
-- Run AFTER 01_schema.sql
-- =============================================================================


-- =============================================================================
-- FIELD
-- =============================================================================

-- -----------------------------------------------------------------------------
-- field_create(p_label)
--   Creates a new field.
--   Raises an exception when label is empty.
--   Returns the created row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION field_create(
    p_label VARCHAR(255)
)
RETURNS field
LANGUAGE plpgsql
AS $$
DECLARE
    v_row field;
BEGIN
    IF p_label IS NULL OR TRIM(p_label) = '' THEN
        RAISE EXCEPTION 'field.label must not be empty';
    END IF;

    INSERT INTO field (label)
    VALUES (TRIM(p_label))
    RETURNING * INTO v_row;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- field_get_by_id(p_id)
--   Returns the field with the given id.
--   Raises NOT_FOUND when absent.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION field_get_by_id(
    p_id INT
)
RETURNS field
LANGUAGE plpgsql
AS $$
DECLARE
    v_row field;
BEGIN
    SELECT * INTO v_row FROM field WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'field.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- field_list()
--   Returns all fields ordered by id.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION field_list()
RETURNS SETOF field
LANGUAGE sql
STABLE
AS $$
    SELECT * FROM field ORDER BY id;
$$;

-- -----------------------------------------------------------------------------
-- field_update(p_id, p_label)
--   Updates the label of an existing field.
--   Raises an exception when label is empty or field does not exist.
--   Returns the updated row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION field_update(
    p_id    INT,
    p_label VARCHAR(255)
)
RETURNS field
LANGUAGE plpgsql
AS $$
DECLARE
    v_row field;
BEGIN
    IF p_label IS NULL OR TRIM(p_label) = '' THEN
        RAISE EXCEPTION 'field.label must not be empty';
    END IF;

    UPDATE field
    SET    label = TRIM(p_label)
    WHERE  id = p_id
    RETURNING * INTO v_row;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'field.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- field_delete(p_id)
--   Deletes a field.
--   Raises an exception when the field does not exist or is still referenced
--   by a non-cancelled match.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE field_delete(
    p_id INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_active_count INT;
BEGIN
    -- Guard: field must exist
    IF NOT EXISTS (SELECT 1 FROM field WHERE id = p_id) THEN
        RAISE EXCEPTION 'field.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    -- Guard: no active match on this field
    SELECT COUNT(*) INTO v_active_count
    FROM   match
    WHERE  field_id    = p_id
    AND    match_state <> 'CANCELLED';

    IF v_active_count > 0 THEN
        RAISE EXCEPTION
            'Cannot delete field.id=% — % active match(es) reference it',
            p_id, v_active_count;
    END IF;

    DELETE FROM field WHERE id = p_id;
END;
$$;


-- =============================================================================
-- TEAM
-- =============================================================================

-- -----------------------------------------------------------------------------
-- team_create(p_tag, p_label, p_state)
--   Creates a new team.
--   Business rules:
--     - tag  must be non-empty and <= 3 characters
--     - label must be non-empty
--     - state must be a valid TeamState value
--   Returns the created row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION team_create(
    p_tag   VARCHAR(3),
    p_label VARCHAR(255),
    p_state team_state_enum DEFAULT 'INCOMPLETE'
)
RETURNS team
LANGUAGE plpgsql
AS $$
DECLARE
    v_row team;
BEGIN
    IF p_tag IS NULL OR TRIM(p_tag) = '' THEN
        RAISE EXCEPTION 'team.tag must not be empty';
    END IF;

    IF LENGTH(TRIM(p_tag)) > 3 THEN
        RAISE EXCEPTION 'team.tag must be at most 3 characters (got "%")', p_tag;
    END IF;

    IF p_label IS NULL OR TRIM(p_label) = '' THEN
        RAISE EXCEPTION 'team.label must not be empty';
    END IF;

    INSERT INTO team (tag, label, team_state)
    VALUES (UPPER(TRIM(p_tag)), TRIM(p_label), p_state)
    RETURNING * INTO v_row;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- team_get_by_id(p_id)
--   Returns the team with the given id.
--   Raises NOT_FOUND when absent.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION team_get_by_id(
    p_id INT
)
RETURNS team
LANGUAGE plpgsql
AS $$
DECLARE
    v_row team;
BEGIN
    SELECT * INTO v_row FROM team WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'team.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- team_list()
--   Returns all teams ordered by id.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION team_list()
RETURNS SETOF team
LANGUAGE sql
STABLE
AS $$
    SELECT * FROM team ORDER BY id;
$$;

-- -----------------------------------------------------------------------------
-- team_update(p_id, p_tag, p_label, p_state)
--   Updates an existing team's attributes.
--   Applies the same invariants as team_create.
--   Returns the updated row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION team_update(
    p_id    INT,
    p_tag   VARCHAR(3),
    p_label VARCHAR(255),
    p_state team_state_enum
)
RETURNS team
LANGUAGE plpgsql
AS $$
DECLARE
    v_row team;
BEGIN
    IF p_tag IS NULL OR TRIM(p_tag) = '' THEN
        RAISE EXCEPTION 'team.tag must not be empty';
    END IF;

    IF LENGTH(TRIM(p_tag)) > 3 THEN
        RAISE EXCEPTION 'team.tag must be at most 3 characters (got "%")', p_tag;
    END IF;

    IF p_label IS NULL OR TRIM(p_label) = '' THEN
        RAISE EXCEPTION 'team.label must not be empty';
    END IF;

    UPDATE team
    SET    tag        = UPPER(TRIM(p_tag)),
           label      = TRIM(p_label),
           team_state = p_state
    WHERE  id = p_id
    RETURNING * INTO v_row;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'team.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- team_delete(p_id)
--   "Soft-deletes" a team by transitioning its state to DISSOUTE.
--   Also cancels every NOT_STARTED or IN_PROGRESS match involving this team
--   (domain rule: a deleted team invalidates its matches).
--   Raises an exception if the team does not exist.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE team_delete(
    p_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Guard: team must exist
    IF NOT EXISTS (SELECT 1 FROM team WHERE id = p_id) THEN
        RAISE EXCEPTION 'team.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    -- Cancel any active matches that involve this team
    UPDATE match
    SET    match_state = 'CANCELLED'
    WHERE  match_state IN ('NOT_STARTED', 'IN_PROGRESS')
    AND    (team_a_id = p_id OR team_b_id = p_id);

    -- Soft-delete: mark team as DISSOUTE
    UPDATE team
    SET    team_state = 'DISSOUTE'
    WHERE  id = p_id;
END;
$$;


-- =============================================================================
-- MATCH
-- =============================================================================

-- -----------------------------------------------------------------------------
-- match_create(p_date, p_start, p_end, p_field_id, p_team_a_id, p_team_b_id)
--   Creates a new match in NOT_STARTED state.
--   Business rules enforced:
--     - p_end > p_start
--     - teamA <> teamB
--     - Neither team may be DISSOUTE
--     - Field must be available (no overlapping non-cancelled match)
--   Returns the created row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION match_create(
    p_date      DATE,
    p_start     TIME,
    p_end       TIME,
    p_field_id  INT,
    p_team_a_id INT,
    p_team_b_id INT
)
RETURNS match
LANGUAGE plpgsql
AS $$
DECLARE
    v_row          match;
    v_team_state   team_state_enum;
    v_overlap      INT;
BEGIN
    -- Period invariant
    IF p_end IS NULL OR p_start IS NULL OR p_end <= p_start THEN
        RAISE EXCEPTION 'match.period: end_time (%) must be after start_time (%)',
            p_end, p_start;
    END IF;

    -- Date must be present
    IF p_date IS NULL THEN
        RAISE EXCEPTION 'match.period.date must not be null';
    END IF;

    -- Teams must be different
    IF p_team_a_id = p_team_b_id THEN
        RAISE EXCEPTION 'match.teamA and match.teamB must be different teams';
    END IF;

    -- Field must exist
    IF NOT EXISTS (SELECT 1 FROM field WHERE id = p_field_id) THEN
        RAISE EXCEPTION 'field.id=% not found', p_field_id
            USING ERRCODE = 'P0002';
    END IF;

    -- TeamA must exist and not be DISSOUTE
    SELECT team_state INTO v_team_state FROM team WHERE id = p_team_a_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'team.id=% (teamA) not found', p_team_a_id
            USING ERRCODE = 'P0002';
    END IF;
    IF v_team_state = 'DISSOUTE' THEN
        RAISE EXCEPTION 'team.id=% (teamA) is DISSOUTE and cannot participate in a match',
            p_team_a_id;
    END IF;

    -- TeamB must exist and not be DISSOUTE
    SELECT team_state INTO v_team_state FROM team WHERE id = p_team_b_id;
    IF NOT FOUND THEN
        RAISE EXCEPTION 'team.id=% (teamB) not found', p_team_b_id
            USING ERRCODE = 'P0002';
    END IF;
    IF v_team_state = 'DISSOUTE' THEN
        RAISE EXCEPTION 'team.id=% (teamB) is DISSOUTE and cannot participate in a match',
            p_team_b_id;
    END IF;

    -- Field availability: no overlapping match on the same field and day
    -- Two slots overlap when: existing.start_time < p_end AND existing.end_time > p_start
    SELECT COUNT(*) INTO v_overlap
    FROM   match
    WHERE  field_id    = p_field_id
    AND    match_date  = p_date
    AND    match_state <> 'CANCELLED'
    AND    start_time  < p_end
    AND    end_time    > p_start;

    IF v_overlap > 0 THEN
        RAISE EXCEPTION
            'field.id=% is not available on % between % and % (% overlapping match(es))',
            p_field_id, p_date, p_start, p_end, v_overlap;
    END IF;

    -- All guards passed — insert
    INSERT INTO match (match_date, start_time, end_time, match_state,
                       field_id, team_a_id, team_b_id)
    VALUES (p_date, p_start, p_end, 'NOT_STARTED',
            p_field_id, p_team_a_id, p_team_b_id)
    RETURNING * INTO v_row;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- match_get_by_id(p_id)
--   Returns the match with the given id.
--   Raises NOT_FOUND when absent.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION match_get_by_id(
    p_id INT
)
RETURNS match
LANGUAGE plpgsql
AS $$
DECLARE
    v_row match;
BEGIN
    SELECT * INTO v_row FROM match WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'match.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- match_list()
--   Returns all matches ordered by match_date, start_time.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION match_list()
RETURNS SETOF match
LANGUAGE sql
STABLE
AS $$
    SELECT * FROM match ORDER BY match_date, start_time;
$$;

-- -----------------------------------------------------------------------------
-- match_start(p_id)
--   Transitions a match from NOT_STARTED → IN_PROGRESS.
--   Business rules:
--     - Match must currently be NOT_STARTED.
--   Returns the updated row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION match_start(
    p_id INT
)
RETURNS match
LANGUAGE plpgsql
AS $$
DECLARE
    v_row         match;
    v_current_state match_state_enum;
BEGIN
    SELECT match_state INTO v_current_state FROM match WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'match.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    IF v_current_state <> 'NOT_STARTED' THEN
        RAISE EXCEPTION
            'Cannot start match.id=% — current state is "%" (expected NOT_STARTED)',
            p_id, v_current_state;
    END IF;

    UPDATE match
    SET    match_state = 'IN_PROGRESS'
    WHERE  id = p_id
    RETURNING * INTO v_row;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- match_end(p_id)
--   Transitions a match from IN_PROGRESS → FINISHED.
--   Business rules:
--     - Match must currently be IN_PROGRESS.
--   Returns the updated row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION match_end(
    p_id INT
)
RETURNS match
LANGUAGE plpgsql
AS $$
DECLARE
    v_row           match;
    v_current_state match_state_enum;
BEGIN
    SELECT match_state INTO v_current_state FROM match WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'match.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    IF v_current_state <> 'IN_PROGRESS' THEN
        RAISE EXCEPTION
            'Cannot end match.id=% — current state is "%" (expected IN_PROGRESS)',
            p_id, v_current_state;
    END IF;

    UPDATE match
    SET    match_state = 'FINISHED'
    WHERE  id = p_id
    RETURNING * INTO v_row;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- match_cancel(p_id)
--   Transitions a match to CANCELLED.
--   Business rules:
--     - Match must be NOT_STARTED or IN_PROGRESS.
--   Returns the updated row.
-- -----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION match_cancel(
    p_id INT
)
RETURNS match
LANGUAGE plpgsql
AS $$
DECLARE
    v_row           match;
    v_current_state match_state_enum;
BEGIN
    SELECT match_state INTO v_current_state FROM match WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'match.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    IF v_current_state NOT IN ('NOT_STARTED', 'IN_PROGRESS') THEN
        RAISE EXCEPTION
            'Cannot cancel match.id=% — current state is "%" (only NOT_STARTED or IN_PROGRESS allowed)',
            p_id, v_current_state;
    END IF;

    UPDATE match
    SET    match_state = 'CANCELLED'
    WHERE  id = p_id
    RETURNING * INTO v_row;

    RETURN v_row;
END;
$$;

-- -----------------------------------------------------------------------------
-- match_delete(p_id)
--   Physically removes a match record.
--   Business rules:
--     - Match must be NOT_STARTED or CANCELLED (cannot delete an active/finished match).
-- -----------------------------------------------------------------------------
CREATE OR REPLACE PROCEDURE match_delete(
    p_id INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_state match_state_enum;
BEGIN
    SELECT match_state INTO v_current_state FROM match WHERE id = p_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'match.id=% not found', p_id
            USING ERRCODE = 'P0002';
    END IF;

    IF v_current_state NOT IN ('NOT_STARTED', 'CANCELLED') THEN
        RAISE EXCEPTION
            'Cannot delete match.id=% — current state is "%" (only NOT_STARTED or CANCELLED allowed)',
            p_id, v_current_state;
    END IF;

    DELETE FROM match WHERE id = p_id;
END;
$$;
