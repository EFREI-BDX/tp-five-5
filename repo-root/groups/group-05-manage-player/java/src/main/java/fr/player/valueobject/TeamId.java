package fr.player.entity;

import java.util.Objects;

/**
 * Représente l'identifiant unique d'une équipe.
 */
public class TeamId {
    private final String value;

    public TeamId(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("TeamId value must not be empty or only whitespace");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
