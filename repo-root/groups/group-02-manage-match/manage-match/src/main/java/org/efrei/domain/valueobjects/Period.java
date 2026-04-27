package org.efrei.domain.valueobjects;
import java.time.LocalDateTime;
public record Period(LocalDateTime start, LocalDateTime end) {
    public Period {
        if (end.isBefore(start)) throw new IllegalArgumentException("La date de fin doit être après le début");
    }
}
