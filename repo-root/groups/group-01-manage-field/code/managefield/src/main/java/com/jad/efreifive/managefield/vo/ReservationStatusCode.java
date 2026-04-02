package com.jad.efreifive.managefield.vo;

import com.jad.efreifive.managefield.exception.InvalidValueObjectException;

import java.io.Serializable;
import java.util.Set;

public record ReservationStatusCode(String value) implements Serializable {

    public static final String PENDING = "pending";
    public static final String CONFIRMED = "confirmed";
    public static final String CANCELLED = "cancelled";

    private static final Set<String> ALLOWED_VALUES = Set.of(PENDING, CONFIRMED, CANCELLED);

    public ReservationStatusCode {
        if (value == null || value.isBlank()) {
            throw new InvalidValueObjectException("reservation status code must not be blank.");
        }
        value = value.trim().toLowerCase();
        if (!ALLOWED_VALUES.contains(value)) {
            throw new InvalidValueObjectException("reservation status code must be one of: pending, confirmed, cancelled.");
        }
    }

    public static ReservationStatusCode of(String rawValue) {
        return new ReservationStatusCode(rawValue);
    }

    public boolean isBlocking() {
        return PENDING.equals(value) || CONFIRMED.equals(value);
    }

    public boolean isCancelled() {
        return CANCELLED.equals(value);
    }
}
