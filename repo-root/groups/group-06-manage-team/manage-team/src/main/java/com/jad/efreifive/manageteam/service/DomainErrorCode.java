package com.jad.efreifive.manageteam.service;

public enum DomainErrorCode {
    UNDEFINED("UNDEF", "Undefined error"),
    TEAM_ID_ALREADY_EXISTS("TAIXT", "A team with this id already exists"),
    TEAM_LABEL_ALREADY_EXISTS("TALXT", "A team with this label already exists"),
    TEAM_TAG_ALREADY_EXISTS("TATXT", "A team with this tag already exists"),
    TEAM_NOT_FOUND("TNFND", "No team with this id exists"),
    TEAM_ALREADY_DISSOLVED("TADIS", "Team is already dissolved"),
    TEAM_NOT_DISSOLVED("TNDIS", "Team is not dissolved"),
    TEAM_LABEL_EMPTY("TLEMP", "Label must not be empty"),
    TEAM_TAG_EMPTY("TTEEM", "Tag must not be empty"),
    TEAM_CREATION_DATE_EMPTY("TCEMP", "Creation date must not be empty"),
    TEAM_DISSOLUTION_DATE_BEFORE_CREATION("TDBFC", "Dissolution date must be greater than or equal to creation date"),
    TEAM_LEADER_INVALID("TLINV", "Team leader must be a valid player id"),
    TEAM_LEADER_NOT_MEMBER("TLNMB", "Team leader must be a member of the team"),
    TEAM_TOO_MANY_PLAYERS("TTMPY", "A team cannot have more than 8 players"),
    PLAYER_ALREADY_EXISTS("PAEXT", "A player with this id already exists"),
    PLAYER_NOT_FOUND("PNFND", "No player with this id exists"),
    PLAYER_DISPLAY_NAME_EMPTY("PDEMP", "Display name must not be empty"),
    PLAYER_IS_TEAM_LEADER("PITLD", "Player is a team leader and cannot be assigned to a team"),
    TEAM_FULL("TFULL", "Team already has 8 players"),
    PLAYERS_JSON_EMPTY("PJEMP", "playersJSON must contain at least one player");

    private final String code;
    private final String message;

    DomainErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static DomainErrorCode fromCode(String code) {
        for (DomainErrorCode value : DomainErrorCode.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}