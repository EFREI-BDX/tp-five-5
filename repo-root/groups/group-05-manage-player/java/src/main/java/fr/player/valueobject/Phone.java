package fr.player.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Représente le numéro de téléphone d'un joueur.
 */
public class Phone {
    private final String value;
    // Pattern for international phone format +33 or 0 followed by digits and optional spaces/hyphens
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(\\+33|0)[1-9](?:[0-9]{8}|[0-9]{8}|[\\s.-]?[0-9]{1}[\\s.-]?[0-9]{2}[\\s.-]?[0-9]{2}[\\s.-]?[0-9]{2}[\\s.-]?[0-9]{2})$");

    public Phone(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone value must not be empty or only whitespace");
        }
        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Phone value must be in valid phone format");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
