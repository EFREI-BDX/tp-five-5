DROP FUNCTION IF EXISTS fiveteam.UUIDToBinary;
DROP FUNCTION IF EXISTS fiveteam.BinaryToUUID;

DROP PROCEDURE IF EXISTS fiveteam.teamLabelCheck;
DROP PROCEDURE IF EXISTS fiveteam.teamTagCheck;
DROP PROCEDURE IF EXISTS fiveteam.teamDateCheck;
DROP PROCEDURE IF EXISTS fiveteam.teamLeaderCheck;
DROP PROCEDURE IF EXISTS fiveteam.teamPlayersCountCheck;

DROP PROCEDURE IF EXISTS fiveteam.playerDisplayNameCheck;

DROP PROCEDURE IF EXISTS fiveteam.teamCreate;
DROP PROCEDURE IF EXISTS fiveteam.teamDissolve;
DROP PROCEDURE IF EXISTS fiveteam.teamRestore;
DROP PROCEDURE IF EXISTS fiveteam.teamChangeName;
DROP PROCEDURE IF EXISTS fiveteam.teamChangeTag;

DROP PROCEDURE IF EXISTS fiveteam.playersMassiveCreate;
DROP PROCEDURE IF EXISTS fiveteam.playersMassiveUpdate;
DROP PROCEDURE IF EXISTS fiveteam.playerCreate;
DROP PROCEDURE IF EXISTS fiveteam.playerUpdate;
DROP PROCEDURE IF EXISTS fiveteam.playerDelete;
DROP PROCEDURE IF EXISTS fiveteam.playerAssignTeam;
DROP PROCEDURE IF EXISTS fiveteam.playerUnassignTeam;

DELIMITER //

CREATE FUNCTION fiveteam.BinaryToUUID(IN _id BINARY(16)) RETURNS VARCHAR(36)
BEGIN
    RETURN CONCAT(
            HEX(SUBSTRING(_id, 1, 4)), '-',
            HEX(SUBSTRING(_id, 5, 2)), '-',
            HEX(SUBSTRING(_id, 7, 2)), '-',
            HEX(SUBSTRING(_id, 9, 2)), '-',
            HEX(SUBSTRING(_id, 11, 6))
           );
END //

CREATE FUNCTION fiveteam.UUIDToBinary(IN _uuid VARCHAR(36)) RETURNS BINARY(16)
BEGIN
    RETURN UNHEX(REPLACE(_uuid, '-', ''));
END //

CREATE PROCEDURE fiveteam.teamLabelCheck(IN _label VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _label IS NULL OR _label = '' THEN
        SET errorMessage_ = 'Label must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveteam.teamTagCheck(IN _tag VARCHAR(3), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _tag IS NULL OR _tag = '' THEN
        SET errorMessage_ = 'Tag must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveteam.teamDateCheck(IN _creationDate DATE, IN _dissolutionDate DATE, OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';
    IF _creationDate IS NULL THEN
        SET errorMessage_ = 'Creation date must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    IF _dissolutionDate IS NOT NULL AND _dissolutionDate < _creationDate THEN
        SET errorMessage_ = 'Dissolution date must be greater than or equal to creation date';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveteam.teamLeaderCheck(IN _idTeamLeader BINARY(16), IN _idTeam BINARY(16),
                                          OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';
    IF _idTeamLeader IS NOT NULL THEN
        IF (SELECT COUNT(*) FROM fiveteam.player WHERE id = _idTeamLeader) = 0 THEN
            SET errorMessage_ = 'Team leader must be a valid player id';
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = errorMessage_;
        END IF;
        IF (SELECT idTeam FROM fiveteam.player WHERE id = _idTeamLeader) != _idTeam THEN
            SET errorMessage_ = 'Team leader must be a member of the team';
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = errorMessage_;
        END IF;
    END IF;
END //

CREATE PROCEDURE fiveteam.teamPlayersCountCheck(IN _id VARCHAR(36), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE idTeam = @id) > 8 THEN
        SET errorMessage_ = 'A team cannot have more than 8 players';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveteam.playerDisplayNameCheck(IN _displayName VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';
    IF _displayName IS NULL OR _displayName = '' THEN
        SET errorMessage_ = 'Display name must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveteam.teamCreate(IN _id VARCHAR(36),
                                     IN _label VARCHAR(255),
                                     IN _tag VARCHAR(3),
                                     IN _creationDate DATE,
                                     OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE id = @id) > 0 THEN
        SET errorMessage_ = CONCAT('A team with id ', _id, ' already exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    CALL fiveteam.teamLabelCheck(_label, errorMessage_);
    CALL fiveteam.teamTagCheck(_tag, errorMessage_);
    CALL fiveteam.teamDateCheck(_creationDate, NULL, errorMessage_);
    INSERT INTO fiveteam.team (id, label, tag, creationDate, dissolutionDate, idTeamLeader, state)
    VALUES (@id, _label, _tag, _creationDate, NULL, NULL, 'I');
    COMMIT;
END //

CREATE PROCEDURE fiveteam.teamDissolve(IN _id VARCHAR(36), IN _dissolutionDate DATE, OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No team with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    IF (SELECT dissolutionDate FROM fiveteam.team WHERE id = _id) IS NOT NULL THEN
        SET errorMessage_ = CONCAT('Team with id ', _id, ' is already dissolved');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    SELECT creationDate INTO @creationDate FROM fiveteam.team WHERE id = _id;
    CALL fiveteam.teamDateCheck(@creationDate, _dissolutionDate, errorMessage_);
    UPDATE fiveteam.team
    SET dissolutionDate = _dissolutionDate,
        state           = 'D'
    WHERE id = @id;
    COMMIT;
END //


CREATE PROCEDURE fiveteam.teamRestore(IN _id VARCHAR(36), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No team with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    IF (SELECT dissolutionDate FROM fiveteam.team WHERE id = @id) IS NULL THEN
        SET errorMessage_ = CONCAT('Team with id ', _id, ' is not dissolved');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    UPDATE fiveteam.team
    SET dissolutionDate = NULL,
        state           = 'I'
    WHERE id = @id;
    COMMIT;
END //

CREATE PROCEDURE fiveteam.teamChangeName(IN _id VARCHAR(36), IN _newLabel VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No team with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    CALL fiveteam.teamLabelCheck(_newLabel, errorMessage_);
    UPDATE fiveteam.team
    SET label = _newLabel
    WHERE id = @id;
    COMMIT;
END //

CREATE PROCEDURE fiveteam.teamChangeTag(IN _id VARCHAR(36), IN _newTag VARCHAR(3), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No team with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    CALL fiveteam.teamTagCheck(_newTag, errorMessage_);
    UPDATE fiveteam.team
    SET tag = _newTag
    WHERE id = @id;
    COMMIT;
END //

CREATE PROCEDURE fiveteam.playersMassiveCreate(IN _playersJSON LONGTEXT, OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE nbPlayers INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    SET i = 0;
    SET nbPlayers = JSON_LENGTH(_playersJSON);
    IF nbPlayers = 0 THEN
        SET errorMessage_ = 'playersJSON must contain at least one player';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    START TRANSACTION;

    SET errorMessage_ = '';
    WHILE i < nbPlayers
        DO
            SET @id = fiveteam.UUIDToBinary(JSON_UNQUOTE(JSON_EXTRACT(_playersJSON, CONCAT('$[', i, '].id'))));
            SET @displayName = JSON_UNQUOTE(JSON_EXTRACT(_playersJSON, CONCAT('$[', i, '].displayName')));
            INSERT INTO fiveteam.player (id, displayName, idTeam)
            VALUES (@id, @displayName, NULL);
            SET i = i + 1;
        END WHILE;
    COMMIT;
END //

CREATE PROCEDURE fiveteam.playersMassiveUpdate(IN _playersJSON LONGTEXT, OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE nbPlayers INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    SET i = 0;
    SET nbPlayers = JSON_LENGTH(_playersJSON);
    IF nbPlayers = 0 THEN
        SET errorMessage_ = 'playersJSON must contain at least one player';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    START TRANSACTION;

    SET errorMessage_ = '';
    WHILE i < nbPlayers
        DO
            SET @id = fiveteam.UUIDToBinary(JSON_UNQUOTE(JSON_EXTRACT(_playersJSON, CONCAT('$[', i, '].id'))));
            SET @displayName = JSON_UNQUOTE(JSON_EXTRACT(_playersJSON, CONCAT('$[', i, '].displayName')));
            UPDATE fiveteam.player
            SET displayName = @displayName
            WHERE id = @id;
            SET i = i + 1;
        END WHILE;
    COMMIT;
END //

CREATE PROCEDURE fiveteam.playerCreate(IN _id VARCHAR(36), IN _displayName VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE id = @id) > 0 THEN
        SET errorMessage_ = CONCAT('A player with id ', _id, ' already exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    CALL fiveteam.playerDisplayNameCheck(_displayName, errorMessage_);
    INSERT INTO fiveteam.player (id, displayName, idTeam)
    VALUES (@id, _displayName, NULL);
    COMMIT;
END //

CREATE PROCEDURE fiveteam.playerUpdate(IN _id VARCHAR(36), IN _displayName VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No player with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    CALL fiveteam.playerDisplayNameCheck(_displayName, errorMessage_);
    UPDATE fiveteam.player
    SET displayName = _displayName
    WHERE id = @id;
    COMMIT;
END //

CREATE PROCEDURE fiveteam.playerDelete(IN _id VARCHAR(36), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No player with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    DELETE
    FROM fiveteam.player
    WHERE id = @id;
    COMMIT;
END //

CREATE PROCEDURE fiveteam.playerAssignTeam(IN _id VARCHAR(36), IN _idTeam VARCHAR(36), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    SET @idTeam = fiveteam.UUIDToBinary(_idTeam);
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No player with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE id = @idTeam) = 0 THEN
        SET errorMessage_ = CONCAT('No team with id ', _idTeam, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE idTeamLeader = @id) > 0 THEN
        SET errorMessage_ = CONCAT('Player with id ', _id, ' is a team leader and cannot be assigned to a team');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE idTeam = @idTeam) >= 8 THEN
        SET errorMessage_ = CONCAT('Team with id ', _idTeam, ' already has 8 players');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    UPDATE fiveteam.player
    SET idTeam = @idTeam
    WHERE id = @id;
    COMMIT;
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE id = @idTeam AND idTeamLeader IS NULL) > 0 THEN
        UPDATE fiveteam.team
        SET idTeamLeader = @id
        WHERE id = @idTeam;
    END IF;
END //

CREATE PROCEDURE fiveteam.playerUnassignTeam(IN _id VARCHAR(36), OUT errorMessage_ VARCHAR(500))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';
    SET @id = fiveteam.UUIDToBinary(_id);
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE id = @id) = 0 THEN
        SET errorMessage_ = CONCAT('No player with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    IF (SELECT COUNT(*) FROM fiveteam.team WHERE idTeamLeader = @id) > 0 THEN
        SET errorMessage_ = CONCAT('Player with id ', _id, ' is a team leader and cannot be assigned to a team');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
    SET @idTeam = (SELECT idTeam FROM fiveteam.player WHERE id = @id);
    UPDATE fiveteam.player
    SET idTeam = NULL
    WHERE id = @id;
    COMMIT;
    IF (SELECT COUNT(*) FROM fiveteam.player WHERE idTeam = @idTeam) < 5 THEN
        UPDATE fiveteam.team
        SET state = 'I'
        WHERE id = @idTeam
          AND state = 'A';
    END IF;
END //
DELIMITER ;

GRANT EXECUTE ON PROCEDURE fiveteam.teamCreate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.teamDissolve TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.teamRestore TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.teamChangeName TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.teamChangeTag TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.playersMassiveCreate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.playersMassiveUpdate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.playerCreate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.playerUpdate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.playerDelete TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.playerAssignTeam TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveteam.playerUnassignTeam TO 'jad_efrei_five_2526'@'%';

