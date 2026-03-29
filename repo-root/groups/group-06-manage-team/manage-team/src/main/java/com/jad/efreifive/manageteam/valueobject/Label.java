package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotBlank;

/**
 * Value-object représentant un libellé (dénomination d'une équipe).
 * La valeur doit être une chaîne non vide.
 */
public record Label(@NotBlank String value) {

    /**
     * Constructeur compact : valide que la valeur n'est pas vide ou blanche.
     */
    public Label {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Label.value must not be blank");
        }
        value = value.trim();
    }

    /**
     * Fabrique une instance à partir d'une chaîne.
     */
    public static Label of(final String value) {
        return new Label(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

