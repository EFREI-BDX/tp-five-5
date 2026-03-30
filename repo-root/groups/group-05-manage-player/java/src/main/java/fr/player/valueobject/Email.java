package fr.player.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Représente l'adresse email d'un joueur.
 */
public class Email {
    private final String value;
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email value must not be empty or only whitespace");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Email value must be a valid email format");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
