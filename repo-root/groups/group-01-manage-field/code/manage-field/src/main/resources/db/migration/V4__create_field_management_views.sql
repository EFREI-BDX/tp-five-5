-- ============================================
-- V4__create_field_management_views.sql
-- ============================================

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_field_status AS
SELECT id, code, label, created_at, updated_at
FROM field_status
WHERE deleted_at IS NULL;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_reservation_status AS
SELECT id, code, label, created_at, updated_at
FROM reservation_status
WHERE deleted_at IS NULL;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_field AS
SELECT id, name, status_id, created_at, updated_at
FROM field
WHERE deleted_at IS NULL;

CREATE OR REPLACE SQL SECURITY DEFINER VIEW v_reservation AS
SELECT
    id,
    field_id,
    status_id,
    reservation_date AS date,
    start_time,
    end_time,
    created_at,
    updated_at
FROM reservation
WHERE deleted_at IS NULL;
