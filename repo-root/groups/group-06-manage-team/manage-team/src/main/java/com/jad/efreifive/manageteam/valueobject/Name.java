package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Value-object représentant un nom (prénom + nom de famille).
 * <ul>
 *   <li>Chaque composant doit être non vide.</li>
 *   <li>Chaque composant doit commencer par une lettre majuscule.</li>
 *   <li>Chaque composant peut contenir des lettres, des espaces, des tirets et des apostrophes.</li>
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

    /** Pattern partagé pour la validation dans le constructeur compact. */
    private static final java.util.regex.Pattern NAME_PATTERN =
            java.util.regex.Pattern.compile("\\p{Lu}[\\p{L} '\\-]*");

    /**
     * Constructeur compact : valide chaque composant selon les invariants métier.
     */
    public Name {
        validate("firstName", firstName);
        validate("lastName", lastName);
    }

    private static void validate(final String field, final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Name." + field + " must not be blank");
        }
        if (!NAME_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Name." + field + " must start with an uppercase letter and contain only letters, spaces, hyphens or apostrophes"
            );
        }
    }

    /**
     * Fabrique une instance à partir du prénom et du nom.
     */
    public static Name of(final String firstName, final String lastName) {
        return new Name(firstName, lastName);
    }

    /**
     * Retourne le nom complet sous la forme "Prénom Nom".
     */
    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return fullName();
    }
}

