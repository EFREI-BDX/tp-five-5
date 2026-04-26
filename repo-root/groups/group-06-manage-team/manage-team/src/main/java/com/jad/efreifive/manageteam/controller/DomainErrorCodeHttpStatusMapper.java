package com.jad.efreifive.manageteam.controller;

import org.springframework.http.HttpStatus;

public enum DomainErrorCodeHttpStatusMapper {
    TEAM_ALREADY_EXISTS(HttpStatus.CONFLICT),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_LABEL(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    DomainErrorCodeHttpStatusMapper(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public static HttpStatus fromCode(String code) {
        try {
            return DomainErrorCodeHttpStatusMapper.valueOf(code).getHttpStatus();
        } catch (IllegalArgumentException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}