package com.group3.efreifive.recordmatch.dto;

import java.util.UUID;

public record PlayerDto(
        UUID playerId,
        UUID teamId
) {
}
