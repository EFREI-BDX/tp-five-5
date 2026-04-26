package com.jad.efreifive.manageteam.dto;

import java.util.UUID;

public record PlayerDto(
        UUID id,
        String firstName,
        String lastName,
        UUID idTeam
) {
}

