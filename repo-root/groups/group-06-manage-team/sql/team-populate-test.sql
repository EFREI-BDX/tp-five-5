USE fiveteam;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE team;
TRUNCATE TABLE player;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO team (id, label, tag, creationDate, dissolutionDate, idTeamLeader, state)
VALUES (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
        'Team Alpha', 'ALP', '2024-01-01', NULL, NULL, 'A'),
       (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')),
        'Team Beta', 'BET', '2024-02-01', NULL, NULL, 'A'),
       (UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', '')),
        'Team Gamma', 'GAM', '2024-03-01', NULL, NULL, 'A'),
       (UNHEX(REPLACE('44444444-4444-4444-4444-444444444444', '-', '')),
        'Team Delta', 'DEL', '2024-04-01', NULL, NULL, 'A'),
       (UNHEX(REPLACE('55555555-5555-5555-5555-555555555555', '-', '')),
        'Team Epsilon', 'EPS', '2024-05-01', NULL, NULL, 'A');

INSERT INTO player (id, firstName, lastName, idTeam)
VALUES (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '-', '')),
        'Alice', 'Anderson', UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', ''))),
       (UNHEX(REPLACE('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '-', '')),
        'Alex', 'Archer', UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', ''))),
       (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1', '-', '')),
        'Bob', 'Baker', UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', ''))),
       (UNHEX(REPLACE('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2', '-', '')),
        'Bella', 'Brown', UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', ''))),
       (UNHEX(REPLACE('cccccccc-cccc-cccc-cccc-ccccccccccc1', '-', '')),
        'Charlie', 'Clark', UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', ''))),
       (UNHEX(REPLACE('cccccccc-cccc-cccc-cccc-ccccccccccc2', '-', '')),
        'Cathy', 'Cole', UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', ''))),
       (UNHEX(REPLACE('dddddddd-dddd-dddd-dddd-ddddddddddd1', '-', '')),
        'David', 'Dunn', UNHEX(REPLACE('44444444-4444-4444-4444-444444444444', '-', ''))),
       (UNHEX(REPLACE('dddddddd-dddd-dddd-dddd-ddddddddddd2', '-', '')),
        'Diana', 'Dorsey', UNHEX(REPLACE('44444444-4444-4444-4444-444444444444', '-', ''))),
       (UNHEX(REPLACE('ffffffff-ffff-ffff-ffff-fffffffffff1', '-', '')),
        'Franck', 'Foster', NULL),
       (UNHEX(REPLACE('ffffffff-ffff-ffff-ffff-fffffffffff2', '-', '')),
        'Fanny', 'Fleming', NULL);
