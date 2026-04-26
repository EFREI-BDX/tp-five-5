package com.jad.efreifive.manageteam.dto;

import java.time.LocalDate;
import java.util.UUID;

public record TeamDto(
        UUID id,
        String label,
        String tag,
        LocalDate creationDate,
        LocalDate dissolutionDate,
        UUID idTeamLeader,
        String state
) {
    public static String getLabel(final TeamDto teamDto) {
        return teamDto.label;
    }

    public static String getTag(final TeamDto teamDto) {
        return teamDto.tag;
    }

    public static LocalDate getCreationDate(final TeamDto teamDto) {
        return teamDto.creationDate;
    }
}

