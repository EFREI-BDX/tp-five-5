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
    id          binary(16)   not null primary key,
    displayName varchar(255) not null,
    idTeam      binary(16)   null,
    foreign key (idTeam) references fiveteam.team (id)
);

ALTER TABLE fiveteam.team
    ADD CONSTRAINT team_leader_fk FOREIGN KEY (idTeamLeader) REFERENCES fiveteam.player (id);

CREATE VIEW fiveteam.TeamView AS
SELECT `fiveteam`.`team`.`id`              AS `id`,
       `fiveteam`.`team`.`label`           AS `label`,
       `fiveteam`.`team`.`tag`             AS `tag`,
       `fiveteam`.`team`.`creationDate`    AS `creationDate`,
       `fiveteam`.`team`.`dissolutionDate` AS `dissolutionDate`,
       `fiveteam`.`team`.`idTeamLeader`    AS `idTeamLeader`,
       `fiveteam`.`team`.`state`           AS `state`
FROM `fiveteam`.`team`;

CREATE VIEW fiveteam.PlayerView AS
SELECT `fiveteam`.`player`.`id`          AS `id`,
       `fiveteam`.`player`.`displayName` AS `displayName`,
       `fiveteam`.`player`.`idTeam`      AS `idTeam`
FROM `fiveteam`.`player`;

GRANT SELECT ON fiveteam.TeamView TO 'jad_efrei_five_2526'@'%';
GRANT SELECT ON fiveteam.PlayerView TO 'jad_efrei_five_2526'@'%';
FLUSH PRIVILEGES;
COMMIT;