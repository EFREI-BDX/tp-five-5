package fr.efreifive.manageplayer.service;

public class ServiceOperationException extends RuntimeException {
    private final DomainErrorCode domainErrorCode;

    public ServiceOperationException(DomainErrorCode domainErrorCode, String message) {
        super(message);
        this.domainErrorCode = domainErrorCode;
    }

    public DomainErrorCode domainErrorCode() {
        return domainErrorCode;
    }
}
