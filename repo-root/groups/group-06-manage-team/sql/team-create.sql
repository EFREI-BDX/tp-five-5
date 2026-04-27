START TRANSACTION;
DROP DATABASE IF EXISTS fiveteam;
CREATE DATABASE fiveteam;
USE fiveteam;

CREATE TABLE fiveteam.team
(
    id              binary(16)           not null primary key,
    label           varchar(255)         not null,
    tag             varchar(3)           not null,
    creationDate    date                 not null,
    dissolutionDate date                 null,
    idTeamLeader    binary(16)           null,
    state           enum ('A', 'I', 'D') not null
);

CREATE TABLE fiveteam.player
(
    id        binary(16)   not null primary key,
    firstName varchar(255) not null,
    lastName  varchar(255) not null,
    idTeam    binary(16)   null,
    foreign key (idTeam) references fiveteam.team (id)
);

ALTER TABLE fiveteam.team
    ADD CONSTRAINT team_leader_fk FOREIGN KEY (idTeamLeader) REFERENCES fiveteam.player (id);
COMMIT;