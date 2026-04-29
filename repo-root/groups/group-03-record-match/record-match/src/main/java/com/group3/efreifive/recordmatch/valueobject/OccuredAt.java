package com.group3.efreifive.recordmatch.valueobject;

import java.time.LocalDateTime;

public record OccuredAt(LocalDateTime value) {

    public OccuredAt {
        if (value == null) {
            throw new IllegalArgumentException("OccuredAt.value must not be null");
        }
    }

    public static OccuredAt of(final LocalDateTime value) {
        return new OccuredAt(value);
    }

    public static OccuredAt now() {
        return new OccuredAt(LocalDateTime.now());
    }

    public String toIsoString() {
        return this.value.toString();
    }
}
