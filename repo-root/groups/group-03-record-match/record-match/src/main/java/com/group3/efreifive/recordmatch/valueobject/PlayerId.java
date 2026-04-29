package com.group3.efreifive.recordmatch.valueobject;

import java.util.UUID;

public record PlayerId(UUID value) {

    public PlayerId {
        if (value == null) {
            throw new IllegalArgumentException("PlayerId.value must not be null");
        }
    }

    public static PlayerId newId() {
        return new PlayerId(UUID.randomUUID());
    }

    public static PlayerId of(final UUID value) {
        return new PlayerId(value);
    }

    public static PlayerId of(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PlayerId.value must not be blank");
        }
        try {
            return new PlayerId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("PlayerId.value must be a valid UUID, got: " + value, e);
        }
    }

    public String asString() {
        return this.value.toString();
    }
}
