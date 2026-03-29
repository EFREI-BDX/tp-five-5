package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Value object representing a person's name (first name + last name).
 * <ul>
 *   <li>Each component must be non-empty.</li>
 *   <li>Each component must start with an uppercase letter.</li>
 *   <li>Each component may contain letters, spaces, hyphens, and apostrophes.</li>
 * </ul>
 */
public record Name(
        @NotBlank
        @Pattern(
                regexp = "\\p{Lu}[\\p{L} '\\-]*",
                message = "firstName must start with an uppercase letter and contain only letters, spaces, hyphens or apostrophes"
        )
        String firstName,

        @NotBlank
        @Pattern(
                regexp = "\\p{Lu}[\\p{L} '\\-]*",
                message = "lastName must start with an uppercase letter and contain only letters, spaces, hyphens or apostrophes"
        )
        String lastName
) {

    /** Shared pattern used for constructor-level validation. */
    private static final java.util.regex.Pattern NAME_PATTERN =
            java.util.regex.Pattern.compile("\\p{Lu}[\\p{L} '\\-]*");

    /**
     * Compact constructor validating each name component.
     */
    public Name {
        Name.validate("firstName", firstName);
        Name.validate("lastName", lastName);
    }

    private static void validate(final String field, final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Name." + field + " must not be blank");
        }
        if (!Name.NAME_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Name." + field + " must start with an uppercase letter and contain only letters, spaces, hyphens or apostrophes"
            );
        }
    }

    /**
     * Creates an instance from first name and last name.
     */
    public static Name of(final String firstName, final String lastName) {
        return new Name(firstName, lastName);
    }

    /**
     * Returns the full name in the "FirstName LastName" format.
     */
    public String fullName() {
        return this.firstName + " " + this.lastName;
    }

}
