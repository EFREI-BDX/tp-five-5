INSERT INTO field (id, name, status_id)
VALUES
    ('22222222-2222-4222-8222-222222222222', 'Field A', '11111111-1111-4111-8111-111111111111'),
    ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 'Field B', '11111111-1111-4111-8111-111111111111'),
    ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 'Field C', '11111111-1111-4111-8111-111111111111'),
    ('cccccccc-cccc-4ccc-8ccc-cccccccccccc', 'Field D', '77777777-7777-4777-8777-777777777777'),
    ('dddddddd-dddd-4ddd-8ddd-dddddddddddd', 'Field E', '55555555-5555-4555-8555-555555555555'),
    ('eeeeeeee-eeee-4eee-8eee-eeeeeeeeeeee', 'Field F', '11111111-1111-4111-8111-111111111111')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    status_id = VALUES(status_id);
