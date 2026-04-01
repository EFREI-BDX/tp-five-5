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
) {}

