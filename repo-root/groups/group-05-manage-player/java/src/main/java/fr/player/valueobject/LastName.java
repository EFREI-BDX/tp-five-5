package fr.player.valueobject;

import java.util.Objects;

/**
 * Représente le nom d'un joueur.
 */
public class LastName {
    private final String value;
    private static final int MAX_LENGTH = 100;

    public LastName(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("LastName value must not be empty or only whitespace");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("LastName value must not exceed " + MAX_LENGTH + " characters");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
