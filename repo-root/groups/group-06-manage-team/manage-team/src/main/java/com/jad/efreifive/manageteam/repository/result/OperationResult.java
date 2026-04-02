package com.jad.efreifive.manageteam.repository.result;

public record OperationResult(boolean success, String message) {

    public static OperationResult ok() {
        return new OperationResult(true, null);
    }

    public static OperationResult fail(String message) {
        return new OperationResult(false, message);
    }

    public static OperationResult fromMessage(String message) {
        if (message == null || message.isBlank()) {
            return ok();
        }
        return fail(message);
    }
}

