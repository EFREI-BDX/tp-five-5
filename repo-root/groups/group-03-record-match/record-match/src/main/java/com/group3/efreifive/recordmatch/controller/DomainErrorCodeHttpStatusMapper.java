package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.service.DomainErrorCode;
import org.springframework.http.HttpStatus;

enum DomainErrorCodeHttpStatusMapper {
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    MATCH_NOT_FOUND(HttpStatus.NOT_FOUND),
    MATCH_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND),
    UNDEFINED(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    DomainErrorCodeHttpStatusMapper(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public static HttpStatus fromDomainErrorCode(DomainErrorCode code) {
        if (code == null) {
            return HttpStatus.BAD_REQUEST;
        }

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