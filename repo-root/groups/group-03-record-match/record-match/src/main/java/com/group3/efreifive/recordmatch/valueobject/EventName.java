package com.group3.efreifive.recordmatch.valueobject;

public record EventName(String value) {

    public EventName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EventName.value must not be blank");
        }
        value = value.trim();
    }

    public static EventName of(final String value) {
        return new EventName(value);
    }

    @Override
    public String toString() {
        return this.value;
    }
}
