package com.jad.efreifive.manageteam.service;

import lombok.Getter;

public class TeamServiceException extends RuntimeException {
    @Getter
    private final DomainErrorCode domainErrorCode;

    public TeamServiceException(DomainErrorCode domainErrorCode, String message) {
        super(message);
        this.domainErrorCode = domainErrorCode;
    }
}
