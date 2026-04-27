-- ============================================
-- V6__grant_field_management_application_permissions.sql
-- ============================================

GRANT SELECT ON manage_field.v_field_status TO 'fivefield'@'%';
GRANT SELECT ON manage_field.v_reservation_status TO 'fivefield'@'%';
GRANT SELECT ON manage_field.v_field TO 'fivefield'@'%';
GRANT SELECT ON manage_field.v_reservation TO 'fivefield'@'%';

GRANT EXECUTE ON PROCEDURE manage_field.app_create_field TO 'fivefield'@'%';
GRANT EXECUTE ON PROCEDURE manage_field.app_change_field_status TO 'fivefield'@'%';
GRANT EXECUTE ON PROCEDURE manage_field.app_create_reservation TO 'fivefield'@'%';
GRANT EXECUTE ON PROCEDURE manage_field.app_change_reservation_status TO 'fivefield'@'%';
