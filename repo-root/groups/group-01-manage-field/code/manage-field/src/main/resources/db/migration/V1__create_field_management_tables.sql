-- ============================================
-- V1__create_field_management_tables.sql
-- ============================================

CREATE TABLE field_status (
    id CHAR(36) NOT NULL,
    code VARCHAR(32) NOT NULL,
    label VARCHAR(100) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL,
    CONSTRAINT pk_field_status PRIMARY KEY (id),
    CONSTRAINT uq_field_status_code UNIQUE (code),
    CONSTRAINT ck_field_status_id_uuid CHECK (id REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$'),
    CONSTRAINT ck_field_status_code CHECK (code IN ('active', 'inactive', 'maintenance'))
);

CREATE TABLE reservation_status (
    id CHAR(36) NOT NULL,
    code VARCHAR(32) NOT NULL,
    label VARCHAR(100) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL,
    CONSTRAINT pk_reservation_status PRIMARY KEY (id),
    CONSTRAINT uq_reservation_status_code UNIQUE (code),
    CONSTRAINT ck_reservation_status_id_uuid CHECK (id REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$'),
    CONSTRAINT ck_reservation_status_code CHECK (code IN ('pending', 'confirmed', 'cancelled'))
);

CREATE TABLE field (
    id CHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    status_id CHAR(36) NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL,
    CONSTRAINT pk_field PRIMARY KEY (id),
    CONSTRAINT uq_field_name UNIQUE (name),
    CONSTRAINT fk_field_status FOREIGN KEY (status_id) REFERENCES field_status(id),
    CONSTRAINT ck_field_id_uuid CHECK (id REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$'),
    CONSTRAINT ck_field_name_not_blank CHECK (TRIM(name) <> '')
);

CREATE TABLE reservation (
    id CHAR(36) NOT NULL,
    field_id CHAR(36) NOT NULL,
    status_id CHAR(36) NOT NULL,
    reservation_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    deleted_at DATETIME(6) NULL,
    CONSTRAINT pk_reservation PRIMARY KEY (id),
    CONSTRAINT fk_reservation_field FOREIGN KEY (field_id) REFERENCES field(id),
    CONSTRAINT fk_reservation_status FOREIGN KEY (status_id) REFERENCES reservation_status(id),
    CONSTRAINT ck_reservation_id_uuid CHECK (id REGEXP '^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$'),
    CONSTRAINT ck_reservation_times CHECK (
        start_time < end_time
        AND MINUTE(start_time) IN (0, 30)
        AND MINUTE(end_time) IN (0, 30)
        AND SECOND(start_time) = 0
        AND SECOND(end_time) = 0
        AND TIME_TO_SEC(TIMEDIFF(end_time, start_time)) / 60 IN (60, 90, 120)
    )
);

CREATE INDEX idx_field_status_id ON field(status_id);
CREATE INDEX idx_reservation_field_date ON reservation(field_id, reservation_date);
CREATE INDEX idx_reservation_status_id ON reservation(status_id);
