package com.group3.efreifive.recordmatch.service;

public class RecordMatchServiceException extends RuntimeException {

    private final DomainErrorCode domainErrorCode;

    public RecordMatchServiceException(DomainErrorCode domainErrorCode, String message) {
        super(message);
        this.domainErrorCode = domainErrorCode;
    }

    public DomainErrorCode getDomainErrorCode() {
        return this.domainErrorCode;
    }
}