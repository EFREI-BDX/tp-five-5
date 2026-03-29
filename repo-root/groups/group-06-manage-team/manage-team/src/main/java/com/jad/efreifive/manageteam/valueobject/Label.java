package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotBlank;

/**
 * Value object representing a team label.
 * The value must be a non-empty string.
 */
public record Label(@NotBlank String value) {

    /**
     * Compact constructor validating that the value is not blank.
     */
    public Label {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Label.value must not be blank");
        }
        value = value.trim();
    }

    /**
     * Creates an instance from a string.
     */
    public static Label of(final String value) {
        return new Label(value);
    }

}
