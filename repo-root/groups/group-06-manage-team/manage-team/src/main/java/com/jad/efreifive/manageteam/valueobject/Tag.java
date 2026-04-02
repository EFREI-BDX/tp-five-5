package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Value object representing a 3-character tag (acronym or short name).
 * The value must contain only uppercase letters (A-Z) and/or digits (0-9).
 *
 * <p>Valid examples: {@code "PSG"}, {@code "OM1"}, {@code "AB2"}</p>
 */
public record Tag(
        @NotNull
        @Pattern(
                regexp = "[A-Z0-9]{3}",
                message = "Tag.value must be exactly 3 uppercase letters and/or digits"
        )
        String value
) {

    private static final java.util.regex.Pattern TAG_PATTERN =
            java.util.regex.Pattern.compile("[A-Z0-9]{3}");

    /**
     * Compact constructor validating tag format and length.
     */
    public Tag {
        if (value == null) {
            throw new IllegalArgumentException("Tag.value must not be null");
        }
        if (!Tag.TAG_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Tag.value must be exactly 3 uppercase letters and/or digits, got: \"" + value + "\""
            );
        }
    }

    /**
     * Creates a tag from a string.
     */
    public static Tag of(final String value) {
        return new Tag(value);
    }

}
