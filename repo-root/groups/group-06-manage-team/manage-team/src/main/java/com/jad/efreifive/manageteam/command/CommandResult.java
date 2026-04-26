package com.jad.efreifive.manageteam.command;

public record CommandResult<E>(E payload) {
    public static <E> CommandResult<E> withPayLoad(final E payLoad) {
        return new CommandResult<>(payLoad);
    }

    public static <E> CommandResult<E> noPayLoad() {
        return new CommandResult<>(null);
    }

    public E getPayload(final String message) {
        if (this.payload == null) throw new IllegalStateException(message);
        return this.payload;
    }
}
