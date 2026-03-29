package com.jad.efreifive.manageteam.valueobject;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Value-object représentant un identifiant unique (UUID).
 * La valeur est générée automatiquement si aucun UUID n'est fourni.
 */
public record Id(@NotNull UUID value) {

    /**
     * Constructeur compact : valide que le UUID n'est pas null.
     */
    public Id {
        if (value == null) {
            throw new IllegalArgumentException("Id.value must not be null");
        }
    }

    /**
     * Fabrique une nouvelle instance avec un UUID auto-généré.
     */
    public static Id newId() {
        return new Id(UUID.randomUUID());
    }

    /**
     * Fabrique une instance à partir d'un UUID existant.
     */
    public static Id of(final UUID value) {
        return new Id(value);
    }

    /**
     * Fabrique une instance à partir d'une représentation textuelle d'un UUID.
     *
     * @throws IllegalArgumentException si la chaîne n'est pas un UUID valide
     */
    public static Id of(final String value) {
        try {
            return new Id(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Id.value must be a valid UUID, got: " + value, e);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

