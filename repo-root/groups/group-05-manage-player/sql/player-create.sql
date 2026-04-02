START TRANSACTION;
DROP DATABASE IF EXISTS fiveplayer;
CREATE DATABASE fiveplayer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE fiveplayer;

CREATE TABLE fiveplayer.team
(
    id   varchar(255) not null primary key,
    name varchar(100) not null
);

CREATE TABLE fiveplayer.player
(
    id        varchar(255)                                            not null primary key,
    firstName varchar(100)                                            not null,
    lastName  varchar(100)                                            not null,
    email     varchar(255)                                            not null,
    phone     varchar(20)                                             not null,
    gender    enum ('homme', 'femme', 'non binaire', 'non spécifié')  not null,
    birthDate date                                                    not null,
    height    decimal(5, 2)                                           not null,
    status    enum ('actif', 'inactif', 'supprimé')                   not null,
    createdAt datetime                                                not null,
    updatedAt datetime                                                not null,
    constraint player_height_positive_ck check (height > 0),
    constraint player_updated_after_created_ck check (updatedAt >= createdAt)
);

CREATE TABLE fiveplayer.player_statistics
(
    idPlayer      varchar(255) not null primary key,
    matchesPlayed int unsigned not null,
    goalsScored   int unsigned not null,
    assists       int unsigned not null,
    wins          int unsigned not null,
    draws         int unsigned not null,
    mvps          int unsigned not null,
    constraint player_statistics_player_fk foreign key (idPlayer) references fiveplayer.player (id),
    constraint player_statistics_wins_vs_matches_ck check (wins <= matchesPlayed),
    constraint player_statistics_draws_vs_matches_ck check (draws <= matchesPlayed),
    constraint player_statistics_mvps_vs_matches_ck check (mvps <= matchesPlayed)
);

CREATE TABLE fiveplayer.player_team
(
    idPlayer varchar(255) not null,
    idTeam   varchar(255) not null,
    primary key (idPlayer, idTeam),
    constraint player_team_player_fk foreign key (idPlayer) references fiveplayer.player (id),
    constraint player_team_team_fk foreign key (idTeam) references fiveplayer.team (id)
);

COMMIT;
