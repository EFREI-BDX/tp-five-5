package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Value object representing a unique identifier (UUID).
 * The value is generated automatically when no UUID is provided.
 */
public record Id(@NotNull UUID value) {

    /**
     * Compact constructor validating that the UUID is not null.
     */
    public Id {
        if (value == null) {
            throw new IllegalArgumentException("Id.value must not be null");
        }
    }

    /**
     * Creates a new instance with an auto-generated UUID.
     */
    public static Id newId() {
        return new Id(UUID.randomUUID());
    }

    /**
     * Creates an instance from an existing UUID.
     */
    public static Id of(final UUID value) {
        return new Id(value);
    }

    /**
     * Creates an instance from a UUID string.
     *
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static Id of(final String value) {
        try {
            return new Id(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Id.value must be a valid UUID, got: " + value, e);
        }
    }

    public String asString() {
        return this.value.toString();
    }

}
