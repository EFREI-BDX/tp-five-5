package com.jad.efreifive.managefield.vo;

import com.jad.efreifive.managefield.exception.InvalidValueObjectException;

import java.io.Serializable;
import java.util.UUID;

public record Id(UUID value) implements Serializable {

    public Id {
        if (value == null) {
            throw new InvalidValueObjectException("id must be a valid UUID.");
        }
    }

    public static Id of(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new InvalidValueObjectException("id must not be blank.");
        }
        try {
            return new Id(UUID.fromString(rawValue.trim()));
        } catch (IllegalArgumentException exception) {
            throw new InvalidValueObjectException("id must be a valid UUID.");
        }
    }

    public static Id random() {
        return new Id(UUID.randomUUID());
    }

    public String asString() {
        return value.toString();
    }
}
