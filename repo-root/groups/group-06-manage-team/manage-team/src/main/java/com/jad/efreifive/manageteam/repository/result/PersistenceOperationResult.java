package com.jad.efreifive.manageteam.repository.result;

import com.jad.efreifive.manageteam.service.DomainErrorCode;

public record PersistenceOperationResult(
        boolean success,
        DomainErrorCode errorCode,
        String message
) {
    public static PersistenceOperationResult fromMessage(final String message) {
        if (message == null || message.isBlank()) return PersistenceOperationResult.ok();
        return PersistenceOperationResult.fail(message);
    }

    public static PersistenceOperationResult ok() {
        return new PersistenceOperationResult(true, null, null);
    }

    public static PersistenceOperationResult fail(final String message) {
        if (message != null && message.contains(":")) {
            String[] parts = message.split(":", 2);
            return new PersistenceOperationResult(false, DomainErrorCode.fromCode(parts[0]), parts[1]);
        }
        return new PersistenceOperationResult(false, DomainErrorCode.UNDEFINED, message);
    }

    public static boolean getResult(final PersistenceOperationResult result) {
        return result.success;
    }
}