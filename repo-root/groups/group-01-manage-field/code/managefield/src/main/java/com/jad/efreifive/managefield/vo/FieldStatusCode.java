package com.jad.efreifive.managefield.vo;

import com.jad.efreifive.managefield.exception.InvalidValueObjectException;

import java.io.Serializable;
import java.util.Set;

public record FieldStatusCode(String value) implements Serializable {

    public static final String ACTIVE = "active";
    public static final String INACTIVE = "inactive";
    public static final String MAINTENANCE = "maintenance";

    private static final Set<String> ALLOWED_VALUES = Set.of(ACTIVE, INACTIVE, MAINTENANCE);

    public FieldStatusCode {
        if (value == null || value.isBlank()) {
            throw new InvalidValueObjectException("field status code must not be blank.");
        }
        value = value.trim().toLowerCase();
        if (!ALLOWED_VALUES.contains(value)) {
            throw new InvalidValueObjectException("field status code must be one of: active, inactive, maintenance.");
        }
    }

    public static FieldStatusCode of(String rawValue) {
        return new FieldStatusCode(rawValue);
    }

    public boolean isActive() {
        return ACTIVE.equals(value);
    }
}
