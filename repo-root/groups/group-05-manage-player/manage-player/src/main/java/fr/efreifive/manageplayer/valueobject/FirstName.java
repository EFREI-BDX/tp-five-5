package fr.player.valueobject;

import java.util.Objects;

/**
 * Représente le prénom d'un joueur.
 */
public class FirstName {
    private final String value;
    private static final int MAX_LENGTH = 100;

    public FirstName(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("FirstName value must not be empty or only whitespace");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("FirstName value must not exceed " + MAX_LENGTH + " characters");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
