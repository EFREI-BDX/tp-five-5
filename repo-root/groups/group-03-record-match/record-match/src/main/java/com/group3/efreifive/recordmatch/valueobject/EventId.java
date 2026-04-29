package com.group3.efreifive.recordmatch.valueobject;

import java.util.UUID;

public record EventId(UUID value) {

    public EventId {
        if (value == null) {
            throw new IllegalArgumentException("EventId.value must not be null");
        }
    }

    public static EventId newId() {
        return new EventId(UUID.randomUUID());
    }

    public static EventId of(final UUID value) {
        return new EventId(value);
    }

    public static EventId of(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EventId.value must not be blank");
        }
        try {
            return new EventId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("EventId.value must be a valid UUID, got: " + value, e);
        }
    }

    public String asString() {
        return this.value.toString();
    }
}
