-- --------------------------------------------------
-- Jeu de test - Manage Field (MariaDB)
-- Ordre: references -> fields -> reservations
-- --------------------------------------------------

-- field_status (IDs alignes avec ReferenceDataCatalog.java)
INSERT INTO field_status (id, code, label) VALUES
  ('11111111-1111-4111-8111-111111111111', 'active', 'Active'),
  ('77777777-7777-4777-8777-777777777777', 'inactive', 'Inactive'),
  ('55555555-5555-4555-8555-555555555555', 'maintenance', 'Maintenance');

-- reservation_status (IDs alignes avec ReferenceDataCatalog.java)
INSERT INTO reservation_status (id, code, label) VALUES
  ('33333333-3333-4333-8333-333333333333', 'pending', 'Pending'),
  ('66666666-6666-4666-8666-666666666666', 'confirmed', 'Confirmed'),
  ('99999999-9999-4999-8999-999999999999', 'cancelled', 'Cancelled');

-- Fields
INSERT INTO field (id, name, status_id) VALUES
  ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1', 'Field Alpha', '11111111-1111-4111-8111-111111111111'),
  ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa2', 'Field Beta',  '11111111-1111-4111-8111-111111111111'),
  ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa3', 'Field Gamma', '55555555-5555-4555-8555-555555555555'),
  ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa4', 'Field Delta', '77777777-7777-4777-8777-777777777777');

-- Reservations
-- Date 2026-04-10: Alpha bloque sur 10:00-11:30 (confirmed)
-- Date 2026-04-10: Alpha a aussi un slot cancelled qui overlap (autorise)
-- Date 2026-04-10: Beta a deux slots bloquants adjacents (sans overlap)
-- Date 2026-04-11: Beta reserve pour verifier l'isolation par date
INSERT INTO reservation (id, field_id, status_id, date, start_time, end_time) VALUES
  ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbb001', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1', '66666666-6666-4666-8666-666666666666', '2026-04-10', '10:00:00', '11:30:00'),
  ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbb002', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa1', '99999999-9999-4999-8999-999999999999', '2026-04-10', '10:30:00', '12:00:00'),
  ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbb003', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa2', '33333333-3333-4333-8333-333333333333', '2026-04-10', '14:00:00', '15:00:00'),
  ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbb004', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa2', '66666666-6666-4666-8666-666666666666', '2026-04-10', '15:00:00', '16:00:00'),
  ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbb005', 'aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaa2', '66666666-6666-4666-8666-666666666666', '2026-04-11', '10:00:00', '11:00:00');

