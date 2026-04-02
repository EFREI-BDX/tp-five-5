package com.jad.efreifive.manageteam.repository.result;

public record OperationResult(boolean success, String message) {

    public static OperationResult fromMessage(String message) {
        if (message == null || message.isBlank()) {
            return OperationResult.ok();
        }
        return OperationResult.fail(message);
    }

    public static OperationResult ok() {
        return new OperationResult(true, null);
    }

    public static OperationResult fail(String message) {
        return new OperationResult(false, message);
    }
}

