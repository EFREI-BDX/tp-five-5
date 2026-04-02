DELIMITER $$

DROP PROCEDURE IF EXISTS sp_list_field_statuses $$
CREATE PROCEDURE sp_list_field_statuses()
BEGIN
	SELECT fs.id, fs.code, fs.label
	FROM field_status fs
	ORDER BY CASE fs.code
		WHEN 'active' THEN 0
		WHEN 'inactive' THEN 1
		WHEN 'maintenance' THEN 2
		ELSE 99
	END, fs.code;
END $$

DROP PROCEDURE IF EXISTS sp_list_reservation_statuses $$
CREATE PROCEDURE sp_list_reservation_statuses()
BEGIN
	SELECT rs.id, rs.code, rs.label
	FROM reservation_status rs
	ORDER BY CASE rs.code
		WHEN 'pending' THEN 0
		WHEN 'confirmed' THEN 1
		WHEN 'cancelled' THEN 2
		ELSE 99
	END, rs.code;
END $$

DROP PROCEDURE IF EXISTS sp_get_field $$
CREATE PROCEDURE sp_get_field(IN p_field_id CHAR(36))
BEGIN
	IF NOT EXISTS (SELECT 1 FROM field f WHERE f.id = p_field_id) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Field not found.';
	END IF;

	SELECT f.id, f.name, f.status_id
	FROM field f
	WHERE f.id = p_field_id;
END $$

DROP PROCEDURE IF EXISTS sp_list_available_fields $$
CREATE PROCEDURE sp_list_available_fields(
	IN p_date DATE,
	IN p_start_time TIME,
	IN p_end_time TIME
)
BEGIN
	DECLARE v_active_field_status_id CHAR(36);

	IF p_start_time IS NULL OR p_end_time IS NULL OR p_date IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'date, start_time and end_time are required.';
	END IF;

	IF NOT (MINUTE(p_start_time) IN (0, 30) AND MINUTE(p_end_time) IN (0, 30)) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'start_time and end_time must be aligned on a full hour or half hour.';
	END IF;

	IF p_start_time >= p_end_time THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'start_time must be before end_time.';
	END IF;

	IF TIMESTAMPDIFF(MINUTE, p_start_time, p_end_time) NOT IN (60, 90, 120) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'reservation duration must be 60, 90, or 120 minutes.';
	END IF;

	SELECT fs.id
	INTO v_active_field_status_id
	FROM field_status fs
	WHERE fs.code = 'active'
	LIMIT 1;

	IF v_active_field_status_id IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Field status active not found.';
	END IF;

	SELECT f.id, f.name, f.status_id
	FROM field f
	WHERE f.status_id = v_active_field_status_id
	  AND NOT EXISTS (
		SELECT 1
		FROM reservation r
		JOIN reservation_status rs ON rs.id = r.status_id
		WHERE r.field_id = f.id
		  AND r.date = p_date
		  AND rs.code IN ('pending', 'confirmed')
		  AND r.start_time < p_end_time
		  AND r.end_time > p_start_time
	  )
	ORDER BY f.name;
END $$

DROP PROCEDURE IF EXISTS sp_update_field_status $$
CREATE PROCEDURE sp_update_field_status(
	IN p_field_id CHAR(36),
	IN p_status_id CHAR(36)
)
BEGIN
	IF NOT EXISTS (SELECT 1 FROM field f WHERE f.id = p_field_id) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Field not found.';
	END IF;

	IF NOT EXISTS (SELECT 1 FROM field_status fs WHERE fs.id = p_status_id) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Field status not found.';
	END IF;

	UPDATE field
	SET status_id = p_status_id
	WHERE id = p_field_id;

	SELECT f.id, f.name, f.status_id
	FROM field f
	WHERE f.id = p_field_id;
END $$

DROP PROCEDURE IF EXISTS sp_create_reservation $$
CREATE PROCEDURE sp_create_reservation(
	IN p_field_id CHAR(36),
	IN p_status_id CHAR(36),
	IN p_date DATE,
	IN p_start_time TIME,
	IN p_end_time TIME
)
BEGIN
	DECLARE v_field_status_code VARCHAR(50);
	DECLARE v_reservation_status_code VARCHAR(50);
	DECLARE v_overlap_count INT DEFAULT 0;
	DECLARE v_reservation_id CHAR(36);

	IF NOT EXISTS (SELECT 1 FROM field f WHERE f.id = p_field_id) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Field not found.';
	END IF;

	IF p_start_time IS NULL OR p_end_time IS NULL OR p_date IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'date, start_time and end_time are required.';
	END IF;

	IF NOT (MINUTE(p_start_time) IN (0, 30) AND MINUTE(p_end_time) IN (0, 30)) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'start_time and end_time must be aligned on a full hour or half hour.';
	END IF;

	IF p_start_time >= p_end_time THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'start_time must be before end_time.';
	END IF;

	IF TIMESTAMPDIFF(MINUTE, p_start_time, p_end_time) NOT IN (60, 90, 120) THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'reservation duration must be 60, 90, or 120 minutes.';
	END IF;

	SELECT fs.code
	INTO v_field_status_code
	FROM field f
	JOIN field_status fs ON fs.id = f.status_id
	WHERE f.id = p_field_id
	LIMIT 1;

	IF v_field_status_code <> 'active' THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Field is not active and cannot be reserved.';
	END IF;

	SELECT rs.code
	INTO v_reservation_status_code
	FROM reservation_status rs
	WHERE rs.id = p_status_id
	LIMIT 1;

	IF v_reservation_status_code IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Reservation status not found.';
	END IF;

	IF v_reservation_status_code IN ('pending', 'confirmed') THEN
		SELECT COUNT(*)
		INTO v_overlap_count
		FROM reservation r
		JOIN reservation_status rs ON rs.id = r.status_id
		WHERE r.field_id = p_field_id
		  AND r.date = p_date
		  AND rs.code IN ('pending', 'confirmed')
		  AND r.start_time < p_end_time
		  AND r.end_time > p_start_time;

		IF v_overlap_count > 0 THEN
			SIGNAL SQLSTATE '45000'
				SET MESSAGE_TEXT = 'Another active reservation already overlaps this time slot for this field.';
		END IF;
	END IF;

	SET v_reservation_id = LOWER(UUID());

	INSERT INTO reservation (id, field_id, status_id, date, start_time, end_time)
	VALUES (v_reservation_id, p_field_id, p_status_id, p_date, p_start_time, p_end_time);

	SELECT r.id, r.field_id, r.status_id, r.date, r.start_time, r.end_time
	FROM reservation r
	WHERE r.id = v_reservation_id;
END $$

DROP PROCEDURE IF EXISTS sp_update_reservation_status $$
CREATE PROCEDURE sp_update_reservation_status(
	IN p_field_id CHAR(36),
	IN p_reservation_id CHAR(36),
	IN p_status_id CHAR(36)
)
BEGIN
	DECLARE v_date DATE;
	DECLARE v_start_time TIME;
	DECLARE v_end_time TIME;
	DECLARE v_field_status_code VARCHAR(50);
	DECLARE v_new_status_code VARCHAR(50);
	DECLARE v_overlap_count INT DEFAULT 0;

	SELECT r.date, r.start_time, r.end_time
	INTO v_date, v_start_time, v_end_time
	FROM reservation r
	WHERE r.id = p_reservation_id
	  AND r.field_id = p_field_id
	LIMIT 1;

	IF v_date IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Reservation not found for this field.';
	END IF;

	SELECT rs.code
	INTO v_new_status_code
	FROM reservation_status rs
	WHERE rs.id = p_status_id
	LIMIT 1;

	IF v_new_status_code IS NULL THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Reservation status not found.';
	END IF;

	IF v_new_status_code IN ('pending', 'confirmed') THEN
		SELECT fs.code
		INTO v_field_status_code
		FROM field f
		JOIN field_status fs ON fs.id = f.status_id
		WHERE f.id = p_field_id
		LIMIT 1;

		IF v_field_status_code <> 'active' THEN
			SIGNAL SQLSTATE '45000'
				SET MESSAGE_TEXT = 'Field is not active and cannot hold a blocking reservation.';
		END IF;

		SELECT COUNT(*)
		INTO v_overlap_count
		FROM reservation r
		JOIN reservation_status rs ON rs.id = r.status_id
		WHERE r.field_id = p_field_id
		  AND r.date = v_date
		  AND r.id <> p_reservation_id
		  AND rs.code IN ('pending', 'confirmed')
		  AND r.start_time < v_end_time
		  AND r.end_time > v_start_time;

		IF v_overlap_count > 0 THEN
			SIGNAL SQLSTATE '45000'
				SET MESSAGE_TEXT = 'Another active reservation already overlaps this time slot for this field.';
		END IF;
	END IF;

	UPDATE reservation
	SET status_id = p_status_id
	WHERE id = p_reservation_id
	  AND field_id = p_field_id;

	SELECT r.id, r.field_id, r.status_id, r.date, r.start_time, r.end_time
	FROM reservation r
	WHERE r.id = p_reservation_id
	  AND r.field_id = p_field_id;
END $$

DELIMITER ;

