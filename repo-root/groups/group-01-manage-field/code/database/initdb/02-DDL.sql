# --------------------------------------------------
# -- FIELD_STATUS
# --------------------------------------------------
CREATE TABLE field_status (
                              id CHAR(36) PRIMARY KEY,
                              code VARCHAR(50) NOT NULL,
                              label VARCHAR(100) NOT NULL,

                              UNIQUE (code),

                              CONSTRAINT chk_field_status_code_lower
                                  CHECK (code = LOWER(code)),

                              CONSTRAINT chk_field_status_code_enum
                                  CHECK (code IN ('active', 'inactive', 'maintenance'))
);

# --------------------------------------------------
# -- FIELD
# --------------------------------------------------
CREATE TABLE field (
                       id CHAR(36) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       status_id CHAR(36) NOT NULL,

                       UNIQUE (name),

                       CONSTRAINT fk_field_status
                           FOREIGN KEY (status_id)
                               REFERENCES field_status(id)
);

# --------------------------------------------------
# -- RESERVATION_STATUS
# --------------------------------------------------
CREATE TABLE reservation_status (
                                    id CHAR(36) PRIMARY KEY,
                                    code VARCHAR(50) NOT NULL,
                                    label VARCHAR(100) NOT NULL,

                                    UNIQUE (code),

                                    CONSTRAINT chk_res_status_code_lower
                                        CHECK (code = LOWER(code)),

                                    CONSTRAINT chk_res_status_code_enum
                                        CHECK (code IN ('pending', 'confirmed', 'cancelled'))
);

# --------------------------------------------------
# -- RESERVATION
# --------------------------------------------------
CREATE TABLE reservation (
                             id CHAR(36) PRIMARY KEY,
                             field_id CHAR(36) NOT NULL,
                             status_id CHAR(36) NOT NULL,
                             date DATE NOT NULL,
                             start_time TIME NOT NULL,
                             end_time TIME NOT NULL,

                             CONSTRAINT fk_res_field
                                 FOREIGN KEY (field_id)
                                     REFERENCES field(id),

                             CONSTRAINT fk_res_status
                                 FOREIGN KEY (status_id)
                                     REFERENCES reservation_status(id),

                            # --------------------------------------------------
                            # -- Minutes = 00 ou 30
                            # --------------------------------------------------
                             CONSTRAINT chk_time_boundary
                             CHECK (
                             MINUTE(start_time) IN (0, 30)
                            AND MINUTE(end_time) IN (0, 30)
                            ),

                            #--------------------------------------------------
                            #- Durée = 60 / 90 / 120 minutes
                            #--------------------------------------------------
                            CONSTRAINT chk_duration
                                CHECK (
                                    TIMESTAMPDIFF(MINUTE, start_time, end_time) IN (60, 90, 120)
                                ),

                            #--------------------------------------------------
                            #-- start < end
                            #--------------------------------------------------
                            CONSTRAINT chk_time_order
                                CHECK (start_time < end_time)
);

#--------------------------------------------------
#-- INDEX pour overlap
#--------------------------------------------------
CREATE INDEX idx_res_field_date
    ON reservation(field_id, date);


##--------------------------------------------------
# TRIGGER
##--------------------------------------------------

DELIMITER $$

CREATE TRIGGER trg_reservation_no_overlap
    BEFORE INSERT ON reservation
    FOR EACH ROW
BEGIN
    DECLARE v_count INT;

    -- Vérifie uniquement si la nouvelle réservation n'est pas cancelled
    IF (SELECT code FROM reservation_status WHERE id = NEW.status_id)
        IN ('pending', 'confirmed') THEN

        SELECT COUNT(*)
        INTO v_count
        FROM reservation r
                 JOIN reservation_status rs ON r.status_id = rs.id
        WHERE r.field_id = NEW.field_id
          AND r.date = NEW.date
          AND rs.code IN ('pending', 'confirmed')
          AND (
            NEW.start_time < r.end_time
                AND NEW.end_time > r.start_time
            );

        IF v_count > 0 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Overlap détecté pour ce terrain';
        END IF;
    END IF;
END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER trg_reservation_no_overlap_update
    BEFORE UPDATE ON reservation
    FOR EACH ROW
BEGIN
    DECLARE v_count INT;

    IF (SELECT code FROM reservation_status WHERE id = NEW.status_id)
        IN ('pending', 'confirmed') THEN

        SELECT COUNT(*)
        INTO v_count
        FROM reservation r
                 JOIN reservation_status rs ON r.status_id = rs.id
        WHERE r.field_id = NEW.field_id
          AND r.date = NEW.date
          AND r.id <> NEW.id
          AND rs.code IN ('pending', 'confirmed')
          AND (
            NEW.start_time < r.end_time
                AND NEW.end_time > r.start_time
            );

        IF v_count > 0 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Overlap détecté pour ce terrain';
        END IF;
    END IF;
END$$

DELIMITER ;