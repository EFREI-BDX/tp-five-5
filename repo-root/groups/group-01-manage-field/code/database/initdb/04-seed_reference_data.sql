INSERT INTO field_status (id, code, label)
VALUES
    ('11111111-1111-4111-8111-111111111111', 'active', 'Active'),
    ('77777777-7777-4777-8777-777777777777', 'inactive', 'Inactive'),
    ('55555555-5555-4555-8555-555555555555', 'maintenance', 'Maintenance')
ON DUPLICATE KEY UPDATE
    label = VALUES(label);

INSERT INTO reservation_status (id, code, label)
VALUES
    ('33333333-3333-4333-8333-333333333333', 'pending', 'Pending'),
    ('66666666-6666-4666-8666-666666666666', 'confirmed', 'Confirmed'),
    ('99999999-9999-4999-8999-999999999999', 'cancelled', 'Cancelled')
ON DUPLICATE KEY UPDATE
    label = VALUES(label);

