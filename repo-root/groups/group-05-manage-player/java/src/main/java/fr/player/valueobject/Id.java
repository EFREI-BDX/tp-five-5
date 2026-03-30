package fr.player.valueobject;

import java.util.Objects;

/**
 * Représente l'identifiant unique d'un joueur dans le système ERP.
 */
public class Id {
    private final String value;

    public Id(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Id value must not be empty or only whitespace");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
