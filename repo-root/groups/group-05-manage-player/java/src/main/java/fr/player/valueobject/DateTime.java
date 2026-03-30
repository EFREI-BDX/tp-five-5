package fr.player.valueobject;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Représente une date et heure technique utilisée pour tracer la création et la mise à jour d'un joueur.
 * Format ISO 8601 recommandé.
 */
public class DateTime {
    private final String value;

    public DateTime(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("DateTime value must not be empty or only whitespace");
        }
        
        try {
            // Validate it's a valid ISO 8601 datetime
            Instant.parse(value);
            this.value = value;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("DateTime must be in valid ISO 8601 format");
        }
    }

    public String getValue() {
        return value;
    }
}
