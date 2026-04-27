DROP VIEW IF EXISTS TeamView;
DROP VIEW IF EXISTS PlayerView;

CREATE VIEW TeamView AS
SELECT fiveteam.BinaryToUUID(fiveteam.team.id)           AS id,
       fiveteam.team.label                               AS label,
       fiveteam.team.tag                                 AS tag,
       fiveteam.team.creationDate                        AS creationDate,
       fiveteam.team.dissolutionDate                     AS dissolutionDate,
       fiveteam.BinaryToUUID(fiveteam.team.idTeamLeader) AS idTeamLeader,
       fiveteam.team.state                               AS state
FROM fiveteam.team;

CREATE VIEW PlayerView AS
SELECT fiveteam.BinaryToUUID(fiveteam.player.id)     AS id,
       fiveteam.player.firstName                     AS firstName,
       fiveteam.player.lastName                      AS lastName,
       fiveteam.BinaryToUUID(fiveteam.player.idTeam) AS idTeam
FROM fiveteam.player;

GRANT SELECT ON TeamView TO 'jad_efrei_five_2526'@'%';
GRANT SELECT ON PlayerView TO 'jad_efrei_five_2526'@'%';