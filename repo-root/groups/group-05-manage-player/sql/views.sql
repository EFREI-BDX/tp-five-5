DROP VIEW IF EXISTS fiveplayer.PlayerStatisticsView;
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
GRANT SELECT ON fiveplayer.PlayerStatisticsView TO 'fiveplayer'@'%';

DROP VIEW IF EXISTS fiveplayer.PlayerView;
CREATE VIEW fiveplayer.PlayerView AS
SELECT p.id        AS id,
       p.firstName AS firstName,
       p.lastName  AS lastName,
       p.email     AS email,
       p.phone     AS phone,
       p.gender    AS gender,
       DATE_FORMAT(p.birthDate, '%d/%m/%Y') AS birthDate,
       p.height    AS height,
       JSON_ARRAY() AS teamIds,
       COALESCE(ps.matchesPlayed, 0) AS matchesPlayed,
       COALESCE(ps.goalsScored, 0)   AS goalsScored,
       COALESCE(ps.assists, 0)       AS assists,
       COALESCE(ps.wins, 0)          AS wins,
       COALESCE(ps.losses, 0)        AS losses,
       COALESCE(ps.draws, 0)         AS draws,
       COALESCE(ps.mvps, 0)          AS mvps,
       JSON_OBJECT(
           'matchesPlayed', COALESCE(ps.matchesPlayed, 0),
           'goalsScored', COALESCE(ps.goalsScored, 0),
           'assists', COALESCE(ps.assists, 0),
           'wins', COALESCE(ps.wins, 0),
           'losses', COALESCE(ps.losses, 0),
           'draws', COALESCE(ps.draws, 0),
           'mvps', COALESCE(ps.mvps, 0)
       ) AS statistics,
       p.status AS status,
       DATE_FORMAT(p.createdAt, '%Y-%m-%dT%H:%i:%sZ') AS createdAt,
       DATE_FORMAT(p.updatedAt, '%Y-%m-%dT%H:%i:%sZ') AS updatedAt
FROM fiveplayer.player p
         LEFT JOIN fiveplayer.player_statistics ps ON ps.idPlayer = p.id;
GRANT SELECT ON fiveplayer.PlayerView TO 'fiveplayer'@'%';

FLUSH PRIVILEGES;
