package com.jad.efreifive.manageteam.repository.result;


public record PersistenceOperationResult(boolean success,
                                         String message) {
    public static PersistenceOperationResult ok(final Integer id) {
        return new PersistenceOperationResult(true, null);
    }

    public static PersistenceOperationResult fromMessage(final String message) {
        if (message == null || message.isBlank()) return PersistenceOperationResult.ok();
        return PersistenceOperationResult.fail(message);
    }

    public static PersistenceOperationResult ok() {
        return new PersistenceOperationResult(true, null);
    }

    public static PersistenceOperationResult fail(final String message) {
        return new PersistenceOperationResult(false, message);
    }

    public static boolean getResult(final PersistenceOperationResult result) {
        return result.success;
    }
}

