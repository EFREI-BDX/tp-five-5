package fr.player.entity;

import java.util.Objects;

/**
 * Représente le nom d'une équipe.
 */
public class TeamName {
    private final String value;
    private static final int MAX_LENGTH = 100;

    public TeamName(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("TeamName value must not be empty or only whitespace");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("TeamName value must not exceed " + MAX_LENGTH + " characters");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
