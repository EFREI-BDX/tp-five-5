package com.jad.efreifive.manageteam.service;

public enum DomainErrorCode {
    // Team errors
    TEAM_ALREADY_EXISTS("A team with this id already exists"),
    TEAM_NOT_FOUND("No team with this id exists"),
    TEAM_ALREADY_DISSOLVED("Team is already dissolved"),
    TEAM_NOT_DISSOLVED("Team is not dissolved"),
    TEAM_LABEL_EMPTY("Label must not be empty"),
    TEAM_TAG_EMPTY("Tag must not be empty"),
    TEAM_CREATION_DATE_EMPTY("Creation date must not be empty"),
    TEAM_DISSOLUTION_DATE_BEFORE_CREATION("Dissolution date must be greater than or equal to creation date"),
    TEAM_LEADER_INVALID("Team leader must be a valid player id"),
    TEAM_LEADER_NOT_MEMBER("Team leader must be a member of the team"),
    TEAM_TOO_MANY_PLAYERS("A team cannot have more than 8 players"),

    // Player errors
    PLAYER_ALREADY_EXISTS("A player with this id already exists"),
    PLAYER_NOT_FOUND("No player with this id exists"),
    PLAYER_DISPLAY_NAME_EMPTY("Display name must not be empty"),
    PLAYER_IS_TEAM_LEADER("Player is a team leader and cannot be assigned to a team"),
    TEAM_FULL("Team already has 8 players"),

    // Massive operations
    PLAYERS_JSON_EMPTY("playersJSON must contain at least one player");

    private final String message;

    DomainErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}