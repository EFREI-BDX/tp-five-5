-- ============================================
-- V5__create_field_management_procedures.sql
-- ============================================

DELIMITER //

CREATE PROCEDURE app_create_field(
    IN p_field_id VARCHAR(36),
    IN p_name VARCHAR(100),
    IN p_status_id VARCHAR(36),
    OUT o_sql_code INT,
    OUT o_sql_message VARCHAR(255),
    OUT o_field_id CHAR(36)
)
SQL SECURITY DEFINER
BEGIN
    DECLARE v_field_id CHAR(36);
    DECLARE v_message VARCHAR(255);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_message = MESSAGE_TEXT;
        SET o_sql_code = 1099;
        SET o_sql_message = v_message;
        SET o_field_id = NULL;
    END;

    SET o_sql_code = 0;
    SET o_sql_message = 'field created';
    SET o_field_id = NULL;
    SET v_field_id = LOWER(COALESCE(NULLIF(TRIM(p_field_id), ''), UUID()));

    IF v_field_id NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'field_id must be a valid UUID';
    ELSEIF p_name IS NULL OR TRIM(p_name) = '' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'field name is required';
    ELSEIF p_status_id IS NULL OR LOWER(TRIM(p_status_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'status_id must be a valid UUID';
    ELSEIF NOT EXISTS (SELECT 1 FROM field_status WHERE id = LOWER(TRIM(p_status_id)) AND deleted_at IS NULL) THEN
        SET o_sql_code = 1002;
        SET o_sql_message = 'field status not found';
    ELSEIF EXISTS (SELECT 1 FROM field WHERE name = TRIM(p_name) AND deleted_at IS NULL) THEN
        SET o_sql_code = 1003;
        SET o_sql_message = 'field name already exists';
    ELSE
        INSERT INTO field (id, name, status_id)
        VALUES (v_field_id, TRIM(p_name), LOWER(TRIM(p_status_id)));
        SET o_field_id = v_field_id;
    END IF;
END//

CREATE PROCEDURE app_change_field_status(
    IN p_field_id VARCHAR(36),
    IN p_status_id VARCHAR(36),
    OUT o_sql_code INT,
    OUT o_sql_message VARCHAR(255)
)
SQL SECURITY DEFINER
BEGIN
    DECLARE v_message VARCHAR(255);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_message = MESSAGE_TEXT;
        SET o_sql_code = 1099;
        SET o_sql_message = v_message;
    END;

    SET o_sql_code = 0;
    SET o_sql_message = 'field status changed';

    IF p_field_id IS NULL OR LOWER(TRIM(p_field_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'field_id must be a valid UUID';
    ELSEIF p_status_id IS NULL OR LOWER(TRIM(p_status_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'status_id must be a valid UUID';
    ELSEIF NOT EXISTS (SELECT 1 FROM field WHERE id = LOWER(TRIM(p_field_id)) AND deleted_at IS NULL) THEN
        SET o_sql_code = 1002;
        SET o_sql_message = 'field not found';
    ELSEIF NOT EXISTS (SELECT 1 FROM field_status WHERE id = LOWER(TRIM(p_status_id)) AND deleted_at IS NULL) THEN
        SET o_sql_code = 1002;
        SET o_sql_message = 'field status not found';
    ELSE
        UPDATE field
        SET status_id = LOWER(TRIM(p_status_id))
        WHERE id = LOWER(TRIM(p_field_id))
          AND deleted_at IS NULL;
    END IF;
END//

CREATE PROCEDURE app_create_reservation(
    IN p_reservation_id VARCHAR(36),
    IN p_field_id VARCHAR(36),
    IN p_status_id VARCHAR(36),
    IN p_date DATE,
    IN p_start_time TIME,
    IN p_end_time TIME,
    OUT o_sql_code INT,
    OUT o_sql_message VARCHAR(255),
    OUT o_reservation_id CHAR(36)
)
SQL SECURITY DEFINER
BEGIN
    DECLARE v_reservation_id CHAR(36);
    DECLARE v_target_status_code VARCHAR(32);
    DECLARE v_field_status_code VARCHAR(32);
    DECLARE v_duration_minutes INT;
    DECLARE v_message VARCHAR(255);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_message = MESSAGE_TEXT;
        SET o_sql_code = 1099;
        SET o_sql_message = v_message;
        SET o_reservation_id = NULL;
    END;

    SET o_sql_code = 0;
    SET o_sql_message = 'reservation created';
    SET o_reservation_id = NULL;
    SET v_reservation_id = LOWER(COALESCE(NULLIF(TRIM(p_reservation_id), ''), UUID()));
    SET v_duration_minutes = TIME_TO_SEC(TIMEDIFF(p_end_time, p_start_time)) / 60;

    SELECT rs.code
    INTO v_target_status_code
    FROM reservation_status rs
    WHERE rs.id = LOWER(TRIM(p_status_id))
      AND rs.deleted_at IS NULL
    LIMIT 1;

    SELECT fs.code
    INTO v_field_status_code
    FROM field f
    INNER JOIN field_status fs ON fs.id = f.status_id
    WHERE f.id = LOWER(TRIM(p_field_id))
      AND f.deleted_at IS NULL
      AND fs.deleted_at IS NULL
    LIMIT 1;

    IF v_reservation_id NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'reservation_id must be a valid UUID';
    ELSEIF p_field_id IS NULL OR LOWER(TRIM(p_field_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'field_id must be a valid UUID';
    ELSEIF p_status_id IS NULL OR LOWER(TRIM(p_status_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'status_id must be a valid UUID';
    ELSEIF p_date IS NULL THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'date is required';
    ELSEIF p_start_time IS NULL OR p_end_time IS NULL THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'start_time and end_time are required';
    ELSEIF MINUTE(p_start_time) NOT IN (0, 30)
        OR MINUTE(p_end_time) NOT IN (0, 30)
        OR SECOND(p_start_time) <> 0
        OR SECOND(p_end_time) <> 0
        OR v_duration_minutes NOT IN (60, 90, 120) THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'slot duration must be 60, 90, or 120 minutes on half-hour boundaries';
    ELSEIF v_target_status_code IS NULL THEN
        SET o_sql_code = 1002;
        SET o_sql_message = 'reservation status not found';
    ELSEIF v_field_status_code IS NULL THEN
        SET o_sql_code = 1002;
        SET o_sql_message = 'field not found';
    ELSEIF v_field_status_code <> 'active' THEN
        SET o_sql_code = 1003;
        SET o_sql_message = 'field is not active';
    ELSEIF v_target_status_code IN ('pending', 'confirmed')
        AND EXISTS (
            SELECT 1
            FROM reservation r
            INNER JOIN reservation_status rs ON rs.id = r.status_id
            WHERE r.field_id = LOWER(TRIM(p_field_id))
              AND r.reservation_date = p_date
              AND r.deleted_at IS NULL
              AND rs.code IN ('pending', 'confirmed')
              AND rs.deleted_at IS NULL
              AND r.start_time < p_end_time
              AND p_start_time < r.end_time
        ) THEN
        SET o_sql_code = 1003;
        SET o_sql_message = 'reservation overlaps an active reservation';
    ELSE
        INSERT INTO reservation (id, field_id, status_id, reservation_date, start_time, end_time)
        VALUES (
            v_reservation_id,
            LOWER(TRIM(p_field_id)),
            LOWER(TRIM(p_status_id)),
            p_date,
            p_start_time,
            p_end_time
        );
        SET o_reservation_id = v_reservation_id;
    END IF;
END//

CREATE PROCEDURE app_change_reservation_status(
    IN p_field_id VARCHAR(36),
    IN p_reservation_id VARCHAR(36),
    IN p_status_id VARCHAR(36),
    OUT o_sql_code INT,
    OUT o_sql_message VARCHAR(255)
)
SQL SECURITY DEFINER
BEGIN
    DECLARE v_target_status_code VARCHAR(32);
    DECLARE v_reservation_date DATE;
    DECLARE v_start_time TIME;
    DECLARE v_end_time TIME;
    DECLARE v_message VARCHAR(255);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_message = MESSAGE_TEXT;
        SET o_sql_code = 1099;
        SET o_sql_message = v_message;
    END;

    SET o_sql_code = 0;
    SET o_sql_message = 'reservation status changed';

    SELECT rs.code
    INTO v_target_status_code
    FROM reservation_status rs
    WHERE rs.id = LOWER(TRIM(p_status_id))
      AND rs.deleted_at IS NULL
    LIMIT 1;

    SELECT r.reservation_date, r.start_time, r.end_time
    INTO v_reservation_date, v_start_time, v_end_time
    FROM reservation r
    WHERE r.id = LOWER(TRIM(p_reservation_id))
      AND r.field_id = LOWER(TRIM(p_field_id))
      AND r.deleted_at IS NULL
    LIMIT 1;

    IF p_field_id IS NULL OR LOWER(TRIM(p_field_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'field_id must be a valid UUID';
    ELSEIF p_reservation_id IS NULL OR LOWER(TRIM(p_reservation_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'reservation_id must be a valid UUID';
    ELSEIF p_status_id IS NULL OR LOWER(TRIM(p_status_id)) NOT REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$' THEN
        SET o_sql_code = 1001;
        SET o_sql_message = 'status_id must be a valid UUID';
    ELSEIF v_target_status_code IS NULL THEN
        SET o_sql_code = 1002;
        SET o_sql_message = 'reservation status not found';
    ELSEIF v_reservation_date IS NULL THEN
        SET o_sql_code = 1002;
        SET o_sql_message = 'reservation not found';
    ELSEIF v_target_status_code IN ('pending', 'confirmed')
        AND EXISTS (
            SELECT 1
            FROM reservation r
            INNER JOIN reservation_status rs ON rs.id = r.status_id
            WHERE r.field_id = LOWER(TRIM(p_field_id))
              AND r.id <> LOWER(TRIM(p_reservation_id))
              AND r.reservation_date = v_reservation_date
              AND r.deleted_at IS NULL
              AND rs.code IN ('pending', 'confirmed')
              AND rs.deleted_at IS NULL
              AND r.start_time < v_end_time
              AND v_start_time < r.end_time
        ) THEN
        SET o_sql_code = 1003;
        SET o_sql_message = 'reservation overlaps an active reservation';
    ELSE
        UPDATE reservation
        SET status_id = LOWER(TRIM(p_status_id))
        WHERE id = LOWER(TRIM(p_reservation_id))
          AND field_id = LOWER(TRIM(p_field_id))
          AND deleted_at IS NULL;
    END IF;
END//

DELIMITER ;
