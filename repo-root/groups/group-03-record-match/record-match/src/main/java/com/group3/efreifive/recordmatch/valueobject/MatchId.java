package com.group3.efreifive.recordmatch.valueobject;

import java.util.UUID;

public record MatchId(UUID value) {

    public MatchId {
        if (value == null) {
            throw new IllegalArgumentException("MatchId.value must not be null");
        }
    }

    public static MatchId newId() {
        return new MatchId(UUID.randomUUID());
    }

    public static MatchId of(final UUID value) {
        return new MatchId(value);
    }

    public static MatchId of(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MatchId.value must not be blank");
        }
        try {
            return new MatchId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("MatchId.value must be a valid UUID, got: " + value, e);
        }
    }

    public String asString() {
        return this.value.toString();
    }
}
