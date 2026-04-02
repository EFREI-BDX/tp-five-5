DROP VIEW IF EXISTS fiveplayer.TeamView;
CREATE VIEW fiveplayer.TeamView AS
SELECT `fiveplayer`.`team`.`id`   AS `id`,
       `fiveplayer`.`team`.`name` AS `name`
FROM `fiveplayer`.`team`;
GRANT SELECT ON fiveplayer.TeamView TO 'jad_efrei_five_2526'@'%';

DROP VIEW IF EXISTS fiveplayer.PlayerView;
CREATE VIEW fiveplayer.PlayerView AS
SELECT `fiveplayer`.`player`.`id`        AS `id`,
       `fiveplayer`.`player`.`firstName` AS `firstName`,
       `fiveplayer`.`player`.`lastName`  AS `lastName`,
       `fiveplayer`.`player`.`email`     AS `email`,
       `fiveplayer`.`player`.`phone`     AS `phone`,
       `fiveplayer`.`player`.`gender`    AS `gender`,
       DATE_FORMAT(`fiveplayer`.`player`.`birthDate`, '%d/%m/%Y') AS `birthDate`,
       `fiveplayer`.`player`.`height`    AS `height`,
       `fiveplayer`.`player`.`status`    AS `status`,
       DATE_FORMAT(`fiveplayer`.`player`.`createdAt`, '%Y-%m-%dT%H:%i:%sZ') AS `createdAt`,
       DATE_FORMAT(`fiveplayer`.`player`.`updatedAt`, '%Y-%m-%dT%H:%i:%sZ') AS `updatedAt`
FROM `fiveplayer`.`player`;
GRANT SELECT ON fiveplayer.PlayerView TO 'jad_efrei_five_2526'@'%';

DROP VIEW IF EXISTS fiveplayer.PlayerStatisticsView;
CREATE VIEW fiveplayer.PlayerStatisticsView AS
SELECT `fiveplayer`.`player_statistics`.`idPlayer`      AS `idPlayer`,
       `fiveplayer`.`player_statistics`.`matchesPlayed` AS `matchesPlayed`,
       `fiveplayer`.`player_statistics`.`goalsScored`   AS `goalsScored`,
       `fiveplayer`.`player_statistics`.`assists`       AS `assists`,
       `fiveplayer`.`player_statistics`.`wins`          AS `wins`,
       `fiveplayer`.`player_statistics`.`draws`         AS `draws`,
       `fiveplayer`.`player_statistics`.`mvps`          AS `mvps`
FROM `fiveplayer`.`player_statistics`;
GRANT SELECT ON fiveplayer.PlayerStatisticsView TO 'jad_efrei_five_2526'@'%';

DROP VIEW IF EXISTS fiveplayer.PlayerTeamView;
CREATE VIEW fiveplayer.PlayerTeamView AS
SELECT `fiveplayer`.`player_team`.`idPlayer` AS `idPlayer`,
       `fiveplayer`.`player_team`.`idTeam`   AS `idTeam`
FROM `fiveplayer`.`player_team`;
GRANT SELECT ON fiveplayer.PlayerTeamView TO 'jad_efrei_five_2526'@'%';

FLUSH PRIVILEGES;
