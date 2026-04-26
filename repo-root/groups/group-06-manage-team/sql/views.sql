DROP VIEW IF EXISTS fiveteam.TeamView;
CREATE VIEW fiveteam.TeamView AS
SELECT fiveteam.BinaryToUUID(team.id)            AS `id`,
       `fiveteam`.`team`.`label`                 AS `label`,
       `fiveteam`.`team`.`tag`                   AS `tag`,
       `fiveteam`.`team`.`creationDate`          AS `creationDate`,
       `fiveteam`.`team`.`dissolutionDate`       AS `dissolutionDate`,
       fiveteam.BinaryToUUID(team.idTeamLeader)  AS `idTeamLeader`,
       fiveteam.TeamStateToJavaLabel(team.state) AS `state`
FROM `fiveteam`.`team`;
GRANT SELECT ON fiveteam.PlayerView TO 'jad_efrei_five_2526'@'%';

DROP VIEW IF EXISTS fiveteam.PlayerView;
CREATE VIEW fiveteam.PlayerView AS
SELECT fiveteam.BinaryToUUID(player.id)     AS `id`,
       `fiveteam`.`player`.`displayName`    AS `displayName`,
       fiveteam.BinaryToUUID(player.idTeam) AS `idTeam`
FROM `fiveteam`.`player`;
GRANT SELECT ON fiveteam.PlayerView TO 'jad_efrei_five_2526'@'%';

FLUSH PRIVILEGES;