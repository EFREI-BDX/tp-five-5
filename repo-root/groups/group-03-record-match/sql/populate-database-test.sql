USE fiverecordmatch;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE matchEvent;
TRUNCATE TABLE event;
TRUNCATE TABLE player;
TRUNCATE TABLE `match`;
SET FOREIGN_KEY_CHECKS = 1;

-- TEAM IDS
SET @team_1 = UNHEX(REPLACE('a0a0a0a0-0000-0000-0000-000000000001', '-', ''));
SET @team_2  = UNHEX(REPLACE('b0b0b0b0-0000-0000-0000-000000000001', '-', ''));

-- PLAYER IDS
SET @p_1_1 = UNHEX(REPLACE('a1a1a1a1-0001-0001-0001-000000000001', '-', ''));
SET @p_1_2 = UNHEX(REPLACE('a1a1a1a1-0001-0001-0001-000000000002', '-', ''));
SET @p_1_3 = UNHEX(REPLACE('a1a1a1a1-0001-0001-0001-000000000003', '-', ''));
SET @p_2_1  = UNHEX(REPLACE('b1b1b1b1-0001-0001-0001-000000000001', '-', ''));
SET @p_2_2  = UNHEX(REPLACE('b1b1b1b1-0001-0001-0001-000000000002', '-', ''));
SET @p_2_3  = UNHEX(REPLACE('b1b1b1b1-0001-0001-0001-000000000003', '-', ''));

-- EVENT IDS
SET @e_START = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000001', '-', ''));
SET @e_END   = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000002', '-', ''));
SET @e_HTBGN = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000003', '-', ''));
SET @e_HTEND = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000004', '-', ''));
SET @e_GOAL  = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000005', '-', ''));
SET @e_ASST  = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000006', '-', ''));
SET @e_SHOT  = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000007', '-', ''));
SET @e_YEL   = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000008', '-', ''));
SET @e_RED   = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000009', '-', ''));
SET @e_SUBIN = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000010', '-', ''));
SET @e_SUBOF = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000011', '-', ''));
SET @e_SAVE  = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000012', '-', ''));
SET @e_FOUL  = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000013', '-', ''));
SET @e_SUBEV = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000014', '-', ''));
SET @e_SHBLK = UNHEX(REPLACE('e0e0e0e0-0001-0001-0001-000000000015', '-', ''));

-- MATCH
SET @match_1 = UNHEX(REPLACE('f0f0f0f0-0001-0001-0001-000000000001', '-', ''));

-- MATCH EVENTS
SET @me_1  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000001', '-', ''));
SET @me_2  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000002', '-', ''));
SET @me_3  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000003', '-', ''));
SET @me_4  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000004', '-', ''));
SET @me_5  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000005', '-', ''));
SET @me_6  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000006', '-', ''));
SET @me_7  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000007', '-', ''));
SET @me_8  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000008', '-', ''));
SET @me_9  = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000009', '-', ''));
SET @me_10 = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000010', '-', ''));
SET @me_11 = UNHEX(REPLACE('d1d1d1d1-0001-0001-0001-000000000011', '-', ''));

-- EVENTS
INSERT INTO fiverecordmatch.event (eventId, name, nbPlayers) VALUES
-- 0 joueur
(@e_START, 'START', 0), -- début du match
(@e_END,   'END',   0), -- fin du match
(@e_HTBGN, 'HTBGN', 0), -- début de la mi-temps
(@e_HTEND, 'HTEND', 0), -- fin de la mi-temps
-- 1 joueur
(@e_GOAL,  'GOAL',  1), -- but marqué par un joueur
(@e_ASST,  'ASST',  1), -- passe décisive d'un joueur sur un but
(@e_SHOT,  'SHOT',  1), -- tir cadré d'un joueur
(@e_YEL,   'YEL',   1), -- carton jaune pour un joueur
(@e_RED,   'RED',   1), -- carton rouge pour un joueur
(@e_SUBIN, 'SUBIN', 1), -- entrée d'un joueur
(@e_SUBOF, 'SUBOF', 1), -- sortie d'un joueur
(@e_SAVE,  'SAVE',  1), -- arrêt d'un joueur
-- 2 joueurs
(@e_FOUL,  'FOUL',  2), -- faute commise par deux joueurs
(@e_SUBEV, 'SUBEV', 2), -- remplacement de deux joueurs
(@e_SHBLK, 'SHBLK', 2); -- tir bloqué par un joueur

-- PLAYERS
INSERT INTO fiverecordmatch.player (playerId, teamId) VALUES
(@p_1_1, @team_1),
(@p_1_2, @team_1),
(@p_1_3, @team_1),
(@p_2_1,  @team_2),
(@p_2_2,  @team_2),
(@p_2_3,  @team_2);

-- MATCH
INSERT INTO fiverecordmatch.`match` (matchId, team1Id, team2Id) VALUES
(@match_1, @team_1, @team_2);

-- MATCH EVENTS
INSERT INTO fiverecordmatch.matchEvent (matchEventId, matchId, eventId, player1Id, player2Id, occuredAt) VALUES
(@me_1,  @match_1, @e_SHOT,  @p_1_1, NULL, '2025-06-15 20:01:00'), -- SHOT (1 joueur) : tir cadré de l'attaquant 1
(@me_2,  @match_1, @e_GOAL,  @p_1_1, NULL, '2025-06-15 20:03:00'), -- GOAL (1 joueur) : but de l'attaquant 1
(@me_3,  @match_1, @e_ASST,  @p_1_2, NULL, '2025-06-15 20:03:00'), -- ASST (1 joueur) : passe décisive du milieu 1
(@me_4,  @match_1, @e_SAVE,  @p_2_3, NULL, '2025-06-15 20:10:00'), -- SAVE (1 joueur) : arrêt du gardien 2
(@me_5,  @match_1, @e_FOUL,  @p_2_2, @p_1_1, '2025-06-15 20:11:00'), -- FOUL (2 joueurs) : faute du défenseur 2 sur l'attaquant 1
(@me_6,  @match_1, @e_YEL,   @p_2_2, NULL, '2025-06-15 20:12:00'), -- YEL (1 joueur) : carton jaune pour le défenseur 2 suite à la faute
(@me_7,  @match_1, @e_GOAL,  @p_2_1, NULL, '2025-06-15 20:18:00'), -- GOAL (1 joueur) : but égalisateur de l'attaquant 2
(@me_8,  @match_1, @e_SHBLK, @p_1_1, @p_2_2, '2025-06-15 20:24:00'), -- SHBLK (2 joueurs) : tir de 1 bloqué par 2
(@me_9,  @match_1, @e_SUBEV, @p_1_3, @p_1_2, '2025-06-15 20:30:00'), -- SUBEV (2 joueurs) : le remplaçant 1 entre, le milieu 1 sort
(@me_10, @match_1, @e_SUBIN, @p_1_3, NULL, '2025-06-15 20:30:00'), -- SUBIN (1 joueur) : entrée du remplaçant 1
(@me_11, @match_1, @e_RED,   @p_2_2, NULL, '2025-06-15 20:39:00'); -- RED (1 joueur) : expulsion du défenseur 2
