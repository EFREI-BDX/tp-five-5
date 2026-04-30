DROP VIEW IF EXISTS fiveplayer.PlayerTeamView;
DROP VIEW IF EXISTS fiveplayer.PlayerStatisticsView;
DROP VIEW IF EXISTS fiveplayer.PlayerView;
DROP VIEW IF EXISTS fiveplayer.TeamView;

CREATE VIEW fiveplayer.TeamView AS
SELECT t.id   AS id,
       t.name AS name
FROM fiveplayer.team t;
GRANT SELECT ON fiveplayer.TeamView TO 'jad_efrei_five_2526'@'%';

CREATE VIEW fiveplayer.PlayerView AS
SELECT p.id        AS id,
       p.firstName AS firstName,
       p.lastName  AS lastName,
       p.email     AS email,
       p.phone     AS phone,
       p.gender    AS gender,
       DATE_FORMAT(p.birthDate, '%d/%m/%Y') AS birthDate,
       p.height    AS height,
       p.status    AS status,
       DATE_FORMAT(p.createdAt, '%Y-%m-%dT%H:%i:%sZ') AS createdAt,
       DATE_FORMAT(p.updatedAt, '%Y-%m-%dT%H:%i:%sZ') AS updatedAt
FROM fiveplayer.player p;
GRANT SELECT ON fiveplayer.PlayerView TO 'jad_efrei_five_2526'@'%';

CREATE VIEW fiveplayer.PlayerStatisticsView AS
SELECT ps.idPlayer      AS idPlayer,
       ps.matchesPlayed AS matchesPlayed,
       ps.goalsScored   AS goalsScored,
       ps.assists       AS assists,
       ps.wins          AS wins,
       ps.losses        AS losses,
       ps.draws         AS draws,
       ps.mvps          AS mvps
FROM fiveplayer.player_statistics ps;
GRANT SELECT ON fiveplayer.PlayerStatisticsView TO 'jad_efrei_five_2526'@'%';

CREATE VIEW fiveplayer.PlayerTeamView AS
SELECT pt.idPlayer AS idPlayer,
       pt.idTeam   AS idTeam
FROM fiveplayer.player_team pt;
GRANT SELECT ON fiveplayer.PlayerTeamView TO 'jad_efrei_five_2526'@'%';

FLUSH PRIVILEGES;
