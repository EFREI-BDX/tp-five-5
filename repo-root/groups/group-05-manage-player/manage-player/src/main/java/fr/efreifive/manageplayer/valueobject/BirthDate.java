package fr.player.valueobject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Représente la date de naissance d'un joueur.
 */
public class BirthDate {
    private final String value;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BirthDate(String value) {
        if (value == null || value.isEmpty() || value.trim().isEmpty()) {
            throw new IllegalArgumentException("BirthDate value must not be empty or only whitespace");
        }
        
        try {
            LocalDate date = LocalDate.parse(value, FORMATTER);
            
            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("BirthDate must not be in the future");
            }
            
            this.value = value;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("BirthDate must respect the format dd/MM/yyyy");
        }
    }

    public String getValue() {
        return value;
    }

    public LocalDate getAsLocalDate() {
        return LocalDate.parse(value, FORMATTER);
    }
}
