package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Value-object représentant un tag (acronyme ou diminutif) d'exactement 3 caractères.
 * La valeur doit être composée uniquement de lettres majuscules (A-Z) et/ou de chiffres (0-9).
 *
 * <p>Exemples valides : {@code "PSG"}, {@code "OM1"}, {@code "AB2"}</p>
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
     * Constructeur compact : valide la longueur et le format.
     */
    public Tag {
        if (value == null) {
            throw new IllegalArgumentException("Tag.value must not be null");
        }
        if (!TAG_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Tag.value must be exactly 3 uppercase letters and/or digits, got: \"" + value + "\""
            );
        }
    }

    /**
     * Fabrique un tag à partir d'une chaîne.
     */
    public static Tag of(final String value) {
        return new Tag(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

