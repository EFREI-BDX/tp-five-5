-- ============================================
-- V2__seed_field_management_reference_data.sql
-- ============================================

INSERT INTO field_status (id, code, label)
VALUES
    ('11111111-1111-4111-8111-111111111111', 'active', 'Active'),
    ('11111111-1111-4111-8111-111111111112', 'inactive', 'Inactive'),
    ('11111111-1111-4111-8111-111111111113', 'maintenance', 'Maintenance');

INSERT INTO reservation_status (id, code, label)
VALUES
    ('33333333-3333-4333-8333-333333333331', 'pending', 'Pending'),
    ('33333333-3333-4333-8333-333333333333', 'confirmed', 'Confirmed'),
    ('33333333-3333-4333-8333-333333333334', 'cancelled', 'Cancelled');
