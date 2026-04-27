START TRANSACTION;

DROP DATABASE IF EXISTS fiveplayer;
CREATE DATABASE fiveplayer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fiveplayer;

CREATE TABLE fiveplayer.player
(
    id        char(36)                                               not null primary key,
    firstName varchar(100)                                           not null,
    lastName  varchar(100)                                           not null,
    email     varchar(255)                                           not null,
    phone     varchar(20)                                            not null,
    gender    enum ('homme', 'femme', 'non binaire', 'non spécifié') not null,
    birthDate date                                                   not null,
    height    decimal(5, 2)                                          not null,
    status    enum ('actif', 'inactif', 'supprimé')                  not null,
    createdAt datetime                                               not null,
    updatedAt datetime                                               not null,
    constraint player_height_positive_ck check (height > 0),
    constraint player_updated_after_created_ck check (updatedAt >= createdAt)
);

CREATE TABLE fiveplayer.player_statistics
(
    idPlayer      char(36)     not null primary key,
    matchesPlayed int unsigned not null,
    goalsScored   int unsigned not null,
    assists       int unsigned not null,
    wins          int unsigned not null,
    losses        int unsigned not null,
    draws         int unsigned not null,
    mvps          int unsigned not null,
    constraint player_statistics_player_fk foreign key (idPlayer) references fiveplayer.player (id),
    constraint player_statistics_results_vs_matches_ck check (wins + losses + draws <= matchesPlayed),
    constraint player_statistics_mvps_vs_matches_ck check (mvps <= matchesPlayed)
);

INSERT INTO fiveplayer.player (id, firstName, lastName, email, phone, gender, birthDate, height, status, createdAt, updatedAt)
VALUES ('11111111-1111-4111-8111-111111111111', 'Lionel', 'Messi', 'lionel.messi@example.com', '+33610000001', 'homme', '1987-06-24', 170.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('22222222-2222-4222-8222-222222222222', 'Cristiano', 'Ronaldo', 'cristiano.ronaldo@example.com', '+33610000002', 'homme', '1985-02-05', 187.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('33333333-3333-4333-8333-333333333333', 'Kylian', 'Mbappe', 'kylian.mbappe@example.com', '+33610000003', 'homme', '1998-12-20', 180.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('44444444-4444-4444-8444-444444444444', 'Lamine', 'Yamal', 'lamine.yamal@example.com', '+33610000004', 'homme', '2007-07-13', 179.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('55555555-5555-4555-8555-555555555555', 'Erling', 'Haaland', 'erling.haaland@example.com', '+33610000005', 'homme', '2000-07-21', 195.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('66666666-6666-4666-8666-666666666666', 'Vinicius', 'Junior', 'vinicius.junior@example.com', '+33610000006', 'homme', '2000-07-12', 176.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('77777777-7777-4777-8777-777777777777', 'Jude', 'Bellingham', 'jude.bellingham@example.com', '+33610000007', 'homme', '2003-06-29', 186.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('88888888-8888-4888-8888-888888888888', 'Neymar', 'Junior', 'neymar.junior@example.com', '+33610000008', 'homme', '1992-02-05', 175.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('99999999-9999-4999-8999-999999999999', 'Mohamed', 'Salah', 'mohamed.salah@example.com', '+33610000009', 'homme', '1992-06-15', 175.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 'Kevin', 'De Bruyne', 'kevin.debruyne@example.com', '+33610000010', 'homme', '1991-06-28', 181.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 'Harry', 'Kane', 'harry.kane@example.com', '+33610000011', 'homme', '1993-07-28', 188.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('cccccccc-cccc-4ccc-8ccc-cccccccccccc', 'Antoine', 'Griezmann', 'antoine.griezmann@example.com', '+33610000012', 'homme', '1991-03-21', 176.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('dddddddd-dddd-4ddd-8ddd-dddddddddddd', 'Rodrygo', 'Goes', 'rodrygo.goes@example.com', '+33610000013', 'homme', '2001-01-09', 174.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('eeeeeeee-eeee-4eee-8eee-eeeeeeeeeeee', 'Luka', 'Modric', 'luka.modric@example.com', '+33610000014', 'homme', '1985-09-09', 172.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('ffffffff-ffff-4fff-8fff-ffffffffffff', 'Robert', 'Lewandowski', 'robert.lewandowski@example.com', '+33610000015', 'homme', '1988-08-21', 185.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('12121212-1212-4121-8121-121212121212', 'Royce', 'Openda', 'royce.openda@example.com', '+33610000016', 'homme', '2002-04-21', 164.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('13131313-1313-4131-8131-131313131313', 'Matthieu', 'Villette', 'matthieu.villette@example.com', '+33610000017', 'homme', '2000-10-20', 180.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('14141414-1414-4141-8141-141414141414', 'Luderic', 'Etonde', 'luderic.etonde@example.com', '+33610000018', 'homme', '2000-08-30', 190.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('15151515-1515-4151-8151-151515151515', 'Steve', 'Shamal', 'steve.shamal@example.com', '+33610000019', 'homme', '1996-02-22', 182.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('16161616-1616-4161-8161-161616161616', 'Lassana', 'Diabate', 'lassana.diabate@example.com', '+33610000020', 'homme', '2003-08-21', 178.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('17171717-1717-4171-8171-171717171717', 'Jan', 'Hoekstra', 'jan.hoekstra@example.com', '+33610000021', 'homme', '1998-08-04', 200.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00'),
       ('18181818-1818-4181-8181-181818181818', 'Oualid', 'El Hajjam', 'oualid.elhajjam@example.com', '+33610000022', 'homme', '1991-02-19', 181.00, 'actif', '2026-04-27 08:00:00', '2026-04-27 08:00:00');

INSERT INTO fiveplayer.player_statistics (idPlayer, matchesPlayed, goalsScored, assists, wins, losses, draws, mvps)
VALUES ('11111111-1111-4111-8111-111111111111', 60, 42, 28, 39, 10, 11, 18),
       ('22222222-2222-4222-8222-222222222222', 60, 45, 9, 38, 14, 8, 16),
       ('33333333-3333-4333-8333-333333333333', 54, 39, 12, 36, 11, 7, 14),
       ('44444444-4444-4444-8444-444444444444', 45, 14, 19, 31, 6, 8, 7),
       ('55555555-5555-4555-8555-555555555555', 50, 41, 6, 34, 10, 6, 13),
       ('66666666-6666-4666-8666-666666666666', 48, 22, 21, 32, 8, 8, 10),
       ('77777777-7777-4777-8777-777777777777', 46, 18, 15, 31, 8, 7, 9),
       ('88888888-8888-4888-8888-888888888888', 42, 21, 18, 27, 7, 8, 8),
       ('99999999-9999-4999-8999-999999999999', 52, 30, 18, 34, 9, 9, 12),
       ('aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa', 40, 9, 27, 25, 7, 8, 8),
       ('bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb', 55, 38, 10, 36, 12, 7, 12),
       ('cccccccc-cccc-4ccc-8ccc-cccccccccccc', 47, 17, 16, 29, 9, 9, 7),
       ('dddddddd-dddd-4ddd-8ddd-dddddddddddd', 44, 15, 12, 30, 7, 7, 5),
       ('eeeeeeee-eeee-4eee-8eee-eeeeeeeeeeee', 38, 5, 13, 24, 7, 7, 6),
       ('ffffffff-ffff-4fff-8fff-ffffffffffff', 51, 35, 7, 33, 10, 8, 11),
       ('12121212-1212-4121-8121-121212121212', 17, 7, 3, 9, 4, 4, 2),
       ('13131313-1313-4131-8131-131313131313', 18, 10, 2, 10, 4, 4, 3),
       ('14141414-1414-4141-8141-141414141414', 18, 8, 2, 10, 4, 4, 2),
       ('15151515-1515-4151-8151-151515151515', 18, 5, 6, 10, 4, 4, 2),
       ('16161616-1616-4161-8161-161616161616', 18, 0, 0, 10, 4, 4, 1),
       ('17171717-1717-4171-8171-171717171717', 18, 0, 0, 10, 4, 4, 1),
       ('18181818-1818-4181-8181-181818181818', 18, 1, 2, 10, 4, 4, 1);

COMMIT;
