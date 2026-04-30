package fr.efreifive.manageplayer.controller;

import fr.efreifive.manageplayer.service.DomainErrorCode;
import org.springframework.http.HttpStatus;

public final class DomainErrorCodeHttpStatusMapper {
    private DomainErrorCodeHttpStatusMapper() {
    }

    public static HttpStatus fromDomainErrorCode(DomainErrorCode domainErrorCode) {
        return switch (domainErrorCode) {
            case PLAYER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case PLAYER_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case PLAYER_DELETED, VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case INTERNAL_SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
