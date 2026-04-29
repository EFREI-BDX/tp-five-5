package com.group3.efreifive.recordmatch.valueobject;

import java.util.UUID;

public record TeamId(UUID value) {

    public TeamId {
        if (value == null) {
            throw new IllegalArgumentException("TeamId.value must not be null");
        }
    }

    public static TeamId newId() {
        return new TeamId(UUID.randomUUID());
    }

    public static TeamId of(final UUID value) {
        return new TeamId(value);
    }

    public static TeamId of(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TeamId.value must not be blank");
        }
        try {
            return new TeamId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("TeamId.value must be a valid UUID, got: " + value, e);
        }
    }

    public String asString() {
        return this.value.toString();
    }
}
