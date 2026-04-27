DROP PROCEDURE IF EXISTS fiveplayer.idCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerIdCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerFirstNameCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerLastNameCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerEmailCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerPhoneCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerGenderCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerBirthDateCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerHeightCheck;
DROP PROCEDURE IF EXISTS fiveplayer.playerStatisticsCheck;

DROP PROCEDURE IF EXISTS fiveplayer.playerCreate;
DROP PROCEDURE IF EXISTS fiveplayer.playerUpdate;
DROP PROCEDURE IF EXISTS fiveplayer.playerDelete;
DROP PROCEDURE IF EXISTS fiveplayer.playerStatisticsUpdate;

DROP PROCEDURE IF EXISTS fiveplayer.playerGetAll;
DROP PROCEDURE IF EXISTS fiveplayer.playerGetById;

DELIMITER //

CREATE PROCEDURE fiveplayer.idCheck(IN _id VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _id IS NULL OR TRIM(_id) = '' THEN
        SET errorMessage_ = 'Id must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF CHAR_LENGTH(_id) > 255 THEN
        SET errorMessage_ = 'Id must not exceed 255 characters';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerIdCheck(IN _id VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    CALL fiveplayer.idCheck(_id, errorMessage_);

    IF _id NOT REGEXP '^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$' THEN
        SET errorMessage_ = 'Player id must be a valid UUID';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerFirstNameCheck(IN _firstName VARCHAR(100), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _firstName IS NULL OR TRIM(_firstName) = '' THEN
        SET errorMessage_ = 'FirstName must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF CHAR_LENGTH(_firstName) > 100 THEN
        SET errorMessage_ = 'FirstName must not exceed 100 characters';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerLastNameCheck(IN _lastName VARCHAR(100), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _lastName IS NULL OR TRIM(_lastName) = '' THEN
        SET errorMessage_ = 'LastName must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF CHAR_LENGTH(_lastName) > 100 THEN
        SET errorMessage_ = 'LastName must not exceed 100 characters';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerEmailCheck(IN _email VARCHAR(255), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _email IS NULL OR TRIM(_email) = '' THEN
        SET errorMessage_ = 'Email must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF CHAR_LENGTH(_email) > 255 THEN
        SET errorMessage_ = 'Email must not exceed 255 characters';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _email NOT REGEXP '^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,}$' THEN
        SET errorMessage_ = 'Email must be a valid email format';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerPhoneCheck(IN _phone VARCHAR(20), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _phone IS NULL OR TRIM(_phone) = '' THEN
        SET errorMessage_ = 'Phone must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF CHAR_LENGTH(_phone) > 20 THEN
        SET errorMessage_ = 'Phone must not exceed 20 characters';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _phone NOT REGEXP '^(\\+33|0)[1-9]([0-9]{8}|[0-9 .-]{8,})$' THEN
        SET errorMessage_ = 'Phone must be in valid phone format';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerGenderCheck(IN _gender VARCHAR(20), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _gender IS NULL OR TRIM(_gender) = '' THEN
        SET errorMessage_ = 'Gender must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _gender NOT IN ('homme', 'femme', 'non binaire', 'non spécifié') THEN
        SET errorMessage_ = 'Gender must be one of homme, femme, non binaire, non spécifié';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerBirthDateCheck(IN _birthDate DATE, OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _birthDate IS NULL THEN
        SET errorMessage_ = 'BirthDate must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _birthDate > CURRENT_DATE() THEN
        SET errorMessage_ = 'BirthDate must not be in the future';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerHeightCheck(IN _height DECIMAL(5, 2), OUT errorMessage_ VARCHAR(500))
BEGIN
    SET errorMessage_ = '';

    IF _height IS NULL THEN
        SET errorMessage_ = 'Height must not be empty';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _height <= 0 THEN
        SET errorMessage_ = 'Height value must be strictly positive';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerStatisticsCheck(
    IN _matchesPlayed INT,
    IN _goalsScored INT,
    IN _assists INT,
    IN _wins INT,
    IN _losses INT,
    IN _draws INT,
    IN _mvps INT,
    OUT errorMessage_ VARCHAR(500)
)
BEGIN
    SET errorMessage_ = '';

    IF _matchesPlayed IS NULL OR _goalsScored IS NULL OR _assists IS NULL OR _wins IS NULL OR _losses IS NULL OR _draws IS NULL OR _mvps IS NULL THEN
        SET errorMessage_ = 'Player statistics values must not be null';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _matchesPlayed < 0 OR _goalsScored < 0 OR _assists < 0 OR _wins < 0 OR _losses < 0 OR _draws < 0 OR _mvps < 0 THEN
        SET errorMessage_ = 'Player statistics values must be greater than or equal to 0';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _wins + _losses + _draws > _matchesPlayed THEN
        SET errorMessage_ = 'Wins, losses and draws total cannot be greater than MatchesPlayed';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _mvps > _matchesPlayed THEN
        SET errorMessage_ = 'Mvps cannot be greater than MatchesPlayed';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;
END //

CREATE PROCEDURE fiveplayer.playerCreate(
    IN _id VARCHAR(255),
    IN _firstName VARCHAR(100),
    IN _lastName VARCHAR(100),
    IN _email VARCHAR(255),
    IN _phone VARCHAR(20),
    IN _gender VARCHAR(20),
    IN _birthDate DATE,
    IN _height DECIMAL(5, 2),
    OUT errorMessage_ VARCHAR(500)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';

    CALL fiveplayer.playerIdCheck(_id, errorMessage_);

    IF (SELECT COUNT(*) FROM fiveplayer.player WHERE id = _id) > 0 THEN
        SET errorMessage_ = CONCAT('A player with id ', _id, ' already exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    CALL fiveplayer.playerFirstNameCheck(_firstName, errorMessage_);
    CALL fiveplayer.playerLastNameCheck(_lastName, errorMessage_);
    CALL fiveplayer.playerEmailCheck(_email, errorMessage_);
    CALL fiveplayer.playerPhoneCheck(_phone, errorMessage_);
    CALL fiveplayer.playerGenderCheck(_gender, errorMessage_);
    CALL fiveplayer.playerBirthDateCheck(_birthDate, errorMessage_);
    CALL fiveplayer.playerHeightCheck(_height, errorMessage_);

    INSERT INTO fiveplayer.player (id, firstName, lastName, email, phone, gender, birthDate, height, status, createdAt, updatedAt)
    VALUES (_id, _firstName, _lastName, _email, _phone, _gender, _birthDate, _height, 'actif', UTC_TIMESTAMP(), UTC_TIMESTAMP());

    INSERT INTO fiveplayer.player_statistics (idPlayer, matchesPlayed, goalsScored, assists, wins, losses, draws, mvps)
    VALUES (_id, 0, 0, 0, 0, 0, 0, 0);

    COMMIT;
END //

CREATE PROCEDURE fiveplayer.playerUpdate(
    IN _id VARCHAR(255),
    IN _firstName VARCHAR(100),
    IN _lastName VARCHAR(100),
    IN _email VARCHAR(255),
    IN _phone VARCHAR(20),
    IN _gender VARCHAR(20),
    IN _birthDate DATE,
    IN _height DECIMAL(5, 2),
    OUT errorMessage_ VARCHAR(500)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';

    CALL fiveplayer.playerIdCheck(_id, errorMessage_);

    IF (SELECT COUNT(*) FROM fiveplayer.player WHERE id = _id) = 0 THEN
        SET errorMessage_ = CONCAT('No player with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF (SELECT status FROM fiveplayer.player WHERE id = _id) = 'supprimé' THEN
        SET errorMessage_ = CONCAT('Player with id ', _id, ' is deleted');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _firstName IS NULL AND _lastName IS NULL AND _email IS NULL AND _phone IS NULL AND _gender IS NULL AND _birthDate IS NULL AND _height IS NULL THEN
        SET errorMessage_ = 'At least one field must be provided for update';
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF _firstName IS NOT NULL THEN
        CALL fiveplayer.playerFirstNameCheck(_firstName, errorMessage_);
    END IF;

    IF _lastName IS NOT NULL THEN
        CALL fiveplayer.playerLastNameCheck(_lastName, errorMessage_);
    END IF;

    IF _email IS NOT NULL THEN
        CALL fiveplayer.playerEmailCheck(_email, errorMessage_);
    END IF;

    IF _phone IS NOT NULL THEN
        CALL fiveplayer.playerPhoneCheck(_phone, errorMessage_);
    END IF;

    IF _gender IS NOT NULL THEN
        CALL fiveplayer.playerGenderCheck(_gender, errorMessage_);
    END IF;

    IF _birthDate IS NOT NULL THEN
        CALL fiveplayer.playerBirthDateCheck(_birthDate, errorMessage_);
    END IF;

    IF _height IS NOT NULL THEN
        CALL fiveplayer.playerHeightCheck(_height, errorMessage_);
    END IF;

    UPDATE fiveplayer.player
    SET firstName = COALESCE(_firstName, firstName),
        lastName  = COALESCE(_lastName, lastName),
        email     = COALESCE(_email, email),
        phone     = COALESCE(_phone, phone),
        gender    = COALESCE(_gender, gender),
        birthDate = COALESCE(_birthDate, birthDate),
        height    = COALESCE(_height, height),
        updatedAt = UTC_TIMESTAMP()
    WHERE id = _id;

    COMMIT;
END //

CREATE PROCEDURE fiveplayer.playerDelete(
    IN _id VARCHAR(255),
    OUT errorMessage_ VARCHAR(500)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';

    CALL fiveplayer.playerIdCheck(_id, errorMessage_);

    IF (SELECT COUNT(*) FROM fiveplayer.player WHERE id = _id) = 0 THEN
        SET errorMessage_ = CONCAT('No player with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF (SELECT status FROM fiveplayer.player WHERE id = _id) = 'supprimé' THEN
        SET errorMessage_ = CONCAT('Player with id ', _id, ' is already deleted');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    UPDATE fiveplayer.player
    SET status = 'supprimé',
        updatedAt = UTC_TIMESTAMP()
    WHERE id = _id;

    COMMIT;
END //

CREATE PROCEDURE fiveplayer.playerStatisticsUpdate(
    IN _id VARCHAR(255),
    IN _matchesPlayed INT,
    IN _goalsScored INT,
    IN _assists INT,
    IN _wins INT,
    IN _losses INT,
    IN _draws INT,
    IN _mvps INT,
    OUT errorMessage_ VARCHAR(500)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL;
        END;

    START TRANSACTION;
    SET errorMessage_ = '';

    CALL fiveplayer.playerIdCheck(_id, errorMessage_);

    IF (SELECT COUNT(*) FROM fiveplayer.player WHERE id = _id) = 0 THEN
        SET errorMessage_ = CONCAT('No player with id ', _id, ' exists');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    IF (SELECT status FROM fiveplayer.player WHERE id = _id) = 'supprimé' THEN
        SET errorMessage_ = CONCAT('Player with id ', _id, ' is deleted');
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = errorMessage_;
    END IF;

    CALL fiveplayer.playerStatisticsCheck(_matchesPlayed, _goalsScored, _assists, _wins, _losses, _draws, _mvps, errorMessage_);

    INSERT INTO fiveplayer.player_statistics (idPlayer, matchesPlayed, goalsScored, assists, wins, losses, draws, mvps)
    VALUES (_id, _matchesPlayed, _goalsScored, _assists, _wins, _losses, _draws, _mvps)
    ON DUPLICATE KEY UPDATE
        matchesPlayed = VALUES(matchesPlayed),
        goalsScored = VALUES(goalsScored),
        assists = VALUES(assists),
        wins = VALUES(wins),
        losses = VALUES(losses),
        draws = VALUES(draws),
        mvps = VALUES(mvps);

    UPDATE fiveplayer.player
    SET updatedAt = UTC_TIMESTAMP()
    WHERE id = _id;

    COMMIT;
END //

CREATE PROCEDURE fiveplayer.playerGetAll()
BEGIN
    SELECT id,
           firstName,
           lastName,
           email,
           phone,
           gender,
           birthDate,
           height,
           teamIds,
           statistics,
           status,
           createdAt,
           updatedAt
    FROM fiveplayer.PlayerView
    ORDER BY lastName, firstName;
END //

CREATE PROCEDURE fiveplayer.playerGetById(
    IN _id VARCHAR(255)
)
BEGIN
    DECLARE errorMessage VARCHAR(500);

    CALL fiveplayer.playerIdCheck(_id, errorMessage);

    SELECT id,
           firstName,
           lastName,
           email,
           phone,
           gender,
           birthDate,
           height,
           teamIds,
           statistics,
           status,
           createdAt,
           updatedAt
    FROM fiveplayer.PlayerView
    WHERE id = _id
    LIMIT 1;
END //

DELIMITER ;

GRANT EXECUTE ON PROCEDURE fiveplayer.playerCreate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerUpdate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerDelete TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerStatisticsUpdate TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerGetAll TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerGetById TO 'jad_efrei_five_2526'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerCreate TO 'fiveplayer'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerUpdate TO 'fiveplayer'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerDelete TO 'fiveplayer'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerStatisticsUpdate TO 'fiveplayer'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerGetAll TO 'fiveplayer'@'%';
GRANT EXECUTE ON PROCEDURE fiveplayer.playerGetById TO 'fiveplayer'@'%';

FLUSH PRIVILEGES;
