DROP DATABASE IF EXISTS fiverecordmatch;
CREATE DATABASE fiverecordmatch;
USE fiverecordmatch;

-- CREATE TABLES
CREATE TABLE fiverecordmatch.event
(
    eventId   binary(16)   not null primary key,
    name      varchar(255) not null,
    nbPlayers int          not null
);

CREATE TABLE fiverecordmatch.player
(
    playerId binary(16) not null primary key,
    teamId   binary(16) not null
);

CREATE TABLE fiverecordmatch.match
(
    matchId binary(16) not null primary key,
    team1Id binary(16) not null,
    team2Id binary(16) not null
);

CREATE TABLE fiverecordmatch.matchEvent
(
    matchEventId binary(16) not null primary key,
    matchId      binary(16) not null,
    eventId      binary(16) not null,
    player1Id    binary(16) null,
    player2Id    binary(16) null,
    occuredAt    date       not null,

    foreign key (matchId) references fiverecordmatch.match (matchId),
    foreign key (eventId) references fiverecordmatch.event (eventId),
    foreign key (player1Id) references fiverecordmatch.player (playerId),
    foreign key (player2Id) references fiverecordmatch.player (playerId)
);

-- CONSTRAINTS
ALTER TABLE fiverecordmatch.event
    ADD CONSTRAINT nbPlayers_non_negative CHECK (nbPlayers >= 0);

ALTER TABLE fiverecordmatch.event
    ADD CONSTRAINT nbPlayers_inf_2 CHECK (nbPlayers <= 2);

ALTER TABLE fiverecordmatch.match
    ADD CONSTRAINT team1_team2_different CHECK (team1Id != team2Id);

-- TRIGGERS
DELIMITER $$

CREATE TRIGGER trg_matchEvent_before_insert
BEFORE INSERT ON fiverecordmatch.matchEvent
FOR EACH ROW
BEGIN
    DECLARE v_nbPlayers INT;
    SELECT nbPlayers INTO v_nbPlayers
    FROM fiverecordmatch.event WHERE eventId = NEW.eventId;

    IF v_nbPlayers = 0 AND (NEW.player1Id IS NOT NULL OR NEW.player2Id IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=0 : player1Id et player2Id doivent être NULL';
    END IF;
    IF v_nbPlayers = 1 AND (NEW.player1Id IS NULL OR NEW.player2Id IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=1 : player1Id requis, player2Id doit être NULL';
    END IF;
    IF v_nbPlayers = 2 AND (NEW.player1Id IS NULL OR NEW.player2Id IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=2 : player1Id et player2Id requis';
    END IF;
    IF v_nbPlayers = 2 AND NEW.player1Id = NEW.player2Id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=2 : player1Id et player2Id doivent être différents';
    END IF;
END$$

CREATE TRIGGER trg_matchEvent_before_update
BEFORE UPDATE ON fiverecordmatch.matchEvent
FOR EACH ROW
BEGIN
    DECLARE v_nbPlayers INT;
    SELECT nbPlayers INTO v_nbPlayers
    FROM fiverecordmatch.event WHERE eventId = NEW.eventId;

    IF v_nbPlayers = 0 AND (NEW.player1Id IS NOT NULL OR NEW.player2Id IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=0 : player1Id et player2Id doivent être NULL';
    END IF;
    IF v_nbPlayers = 1 AND (NEW.player1Id IS NULL OR NEW.player2Id IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=1 : player1Id requis, player2Id doit être NULL';
    END IF;
    IF v_nbPlayers = 2 AND (NEW.player1Id IS NULL OR NEW.player2Id IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=2 : player1Id et player2Id requis';
    END IF;
    IF v_nbPlayers = 2 AND NEW.player1Id = NEW.player2Id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'nbPlayers=2 : player1Id et player2Id doivent être différents';
    END IF;
END$$

DELIMITER ;