-- ============================================
-- V8__replace_field_management_read_views.sql
-- ============================================

DROP VIEW IF EXISTS v_blocking_reservation;
DROP VIEW IF EXISTS v_active_field;
DROP VIEW IF EXISTS v_reservation_details;
DROP VIEW IF EXISTS v_field_details;
DROP VIEW IF EXISTS v_reservation;
DROP VIEW IF EXISTS v_field;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_field_details AS
SELECT
    f.id,
    f.name,
    fs.id AS status_id,
    fs.code AS status_code,
    fs.label AS status_label,
    f.created_at,
    f.updated_at
FROM field f
INNER JOIN field_status fs ON fs.id = f.status_id
WHERE f.deleted_at IS NULL
  AND fs.deleted_at IS NULL;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_active_field AS
SELECT
    f.id,
    f.name,
    fs.id AS status_id,
    fs.code AS status_code,
    fs.label AS status_label,
    f.created_at,
    f.updated_at
FROM field f
INNER JOIN field_status fs ON fs.id = f.status_id
WHERE f.deleted_at IS NULL
  AND fs.deleted_at IS NULL
  AND fs.code = 'active';

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_reservation_details AS
SELECT
    r.id,
    r.field_id,
    rs.id AS status_id,
    rs.code AS status_code,
    rs.label AS status_label,
    r.reservation_date AS date,
    r.start_time,
    r.end_time,
    r.created_at,
    r.updated_at
FROM reservation r
INNER JOIN reservation_status rs ON rs.id = r.status_id
WHERE r.deleted_at IS NULL
  AND rs.deleted_at IS NULL;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_blocking_reservation AS
SELECT
    r.id,
    r.field_id,
    rs.id AS status_id,
    rs.code AS status_code,
    rs.label AS status_label,
    r.reservation_date AS date,
    r.start_time,
    r.end_time,
    r.created_at,
    r.updated_at
FROM reservation r
INNER JOIN reservation_status rs ON rs.id = r.status_id
WHERE r.deleted_at IS NULL
  AND rs.deleted_at IS NULL
  AND rs.code IN ('pending', 'confirmed');

GRANT SELECT ON manage_field.v_field_details TO 'fivefield'@'%';
GRANT SELECT ON manage_field.v_active_field TO 'fivefield'@'%';
GRANT SELECT ON manage_field.v_reservation_details TO 'fivefield'@'%';
GRANT SELECT ON manage_field.v_blocking_reservation TO 'fivefield'@'%';
