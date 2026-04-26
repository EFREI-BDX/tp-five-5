package com.jad.efreifive.manageteam.controller;

import com.jad.efreifive.manageteam.service.DomainErrorCode;
import org.springframework.http.HttpStatus;

enum DomainErrorCodeHttpStatusMapper {
    TEAM_ALREADY_EXISTS(HttpStatus.CONFLICT),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND),
    TEAM_ALREADY_DISSOLVED(HttpStatus.CONFLICT),
    TEAM_NOT_DISSOLVED(HttpStatus.CONFLICT),
    TEAM_LABEL_EMPTY(HttpStatus.BAD_REQUEST),
    TEAM_TAG_EMPTY(HttpStatus.BAD_REQUEST),
    TEAM_CREATION_DATE_EMPTY(HttpStatus.BAD_REQUEST),
    TEAM_DISSOLUTION_DATE_BEFORE_CREATION(HttpStatus.BAD_REQUEST),
    TEAM_LEADER_INVALID(HttpStatus.BAD_REQUEST),
    TEAM_LEADER_NOT_MEMBER(HttpStatus.BAD_REQUEST),
    TEAM_TOO_MANY_PLAYERS(HttpStatus.BAD_REQUEST),
    PLAYER_ALREADY_EXISTS(HttpStatus.CONFLICT),
    PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND),
    PLAYER_DISPLAY_NAME_EMPTY(HttpStatus.BAD_REQUEST),
    PLAYER_IS_TEAM_LEADER(HttpStatus.BAD_REQUEST),
    TEAM_FULL(HttpStatus.CONFLICT),
    PLAYERS_JSON_EMPTY(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    DomainErrorCodeHttpStatusMapper(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public static HttpStatus fromDomainErrorCode(DomainErrorCode code) {
        for (DomainErrorCodeHttpStatusMapper mapper : DomainErrorCodeHttpStatusMapper.values()) {
            if (mapper.name().equals(code.name())) {
                return mapper.getHttpStatus();
            }
        }
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}