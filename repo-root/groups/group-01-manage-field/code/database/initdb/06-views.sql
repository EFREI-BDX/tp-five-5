DROP PROCEDURE IF EXISTS sp_list_field_statuses;
DROP PROCEDURE IF EXISTS sp_list_reservation_statuses;
DROP PROCEDURE IF EXISTS sp_get_field;
DROP PROCEDURE IF EXISTS sp_list_available_fields;

DROP VIEW IF EXISTS vw_blocking_reservations;
DROP VIEW IF EXISTS vw_active_fields;
DROP VIEW IF EXISTS vw_fields;
DROP VIEW IF EXISTS vw_reservation_statuses;
DROP VIEW IF EXISTS vw_field_statuses;

# --------------------------------------------------
# -- READ VIEWS
# --------------------------------------------------

CREATE SQL SECURITY DEFINER VIEW vw_field_statuses AS
SELECT
    fs.id,
    fs.code,
    fs.label,
    CASE fs.code
        WHEN 'active' THEN 0
        WHEN 'inactive' THEN 1
        WHEN 'maintenance' THEN 2
        ELSE 99
    END AS sort_order
FROM field_status fs;

CREATE SQL SECURITY DEFINER VIEW vw_reservation_statuses AS
SELECT
    rs.id,
    rs.code,
    rs.label,
    CASE rs.code
        WHEN 'pending' THEN 0
        WHEN 'confirmed' THEN 1
        WHEN 'cancelled' THEN 2
        ELSE 99
    END AS sort_order
FROM reservation_status rs;

CREATE SQL SECURITY DEFINER VIEW vw_fields AS
SELECT
    f.id,
    f.name,
    f.status_id,
    fs.code AS status_code,
    fs.label AS status_label
FROM field f
JOIN field_status fs ON fs.id = f.status_id;

CREATE SQL SECURITY DEFINER VIEW vw_active_fields AS
SELECT
    f.id,
    f.name,
    f.status_id
FROM field f
JOIN field_status fs ON fs.id = f.status_id
WHERE fs.code = 'active';

CREATE SQL SECURITY DEFINER VIEW vw_blocking_reservations AS
SELECT
    r.id,
    r.field_id,
    r.status_id,
    rs.code AS status_code,
    r.date,
    r.start_time,
    r.end_time
FROM reservation r
JOIN reservation_status rs ON rs.id = r.status_id
WHERE rs.code IN ('pending', 'confirmed');
