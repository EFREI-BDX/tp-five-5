package org.efrei.five.apimanagematch.domain.entities;



import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Label;

import java.util.UUID;

public record Field(Id id, Label label) {

    public static UUID getIdValue(Field field) {
        return field.id().value();
    }

    public static String getLabelValue(Field field) {
        return field.label().value();
    }
}