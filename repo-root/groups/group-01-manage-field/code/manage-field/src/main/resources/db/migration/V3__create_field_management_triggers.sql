-- ============================================
-- V3__create_field_management_triggers.sql
-- ============================================

DELIMITER //

CREATE TRIGGER trg_field_status_set_updated_at
BEFORE UPDATE ON field_status
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP(6);
END//

CREATE TRIGGER trg_reservation_status_set_updated_at
BEFORE UPDATE ON reservation_status
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP(6);
END//

CREATE TRIGGER trg_field_set_updated_at
BEFORE UPDATE ON field
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP(6);
END//

CREATE TRIGGER trg_reservation_set_updated_at
BEFORE UPDATE ON reservation
FOR EACH ROW
BEGIN
    SET NEW.updated_at = CURRENT_TIMESTAMP(6);
END//

CREATE TRIGGER trg_field_status_prevent_delete
BEFORE DELETE ON field_status
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'physical delete is not allowed';
END//

CREATE TRIGGER trg_reservation_status_prevent_delete
BEFORE DELETE ON reservation_status
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'physical delete is not allowed';
END//

CREATE TRIGGER trg_field_prevent_delete
BEFORE DELETE ON field
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'physical delete is not allowed';
END//

CREATE TRIGGER trg_reservation_prevent_delete
BEFORE DELETE ON reservation
FOR EACH ROW
BEGIN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'physical delete is not allowed';
END//

DELIMITER ;
