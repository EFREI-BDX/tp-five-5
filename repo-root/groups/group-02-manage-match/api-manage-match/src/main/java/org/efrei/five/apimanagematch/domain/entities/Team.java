package org.efrei.five.apimanagematch.domain.entities;


import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Label;
import org.efrei.five.apimanagematch.domain.valueobjects.Tag;

import java.util.UUID;

public record Team(Id id, Tag tag, Label label) {
    public static UUID getIdValue(Team team) {
        return team.id.value();
    }

    public static String getTagValue(Team team) {
        return team.tag.value();
    }

    public static String getLabelValue(Team team) {
        return team.label.value();
    }
}
