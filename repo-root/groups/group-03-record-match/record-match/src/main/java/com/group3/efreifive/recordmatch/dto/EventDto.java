package com.group3.efreifive.recordmatch.dto;

import java.util.UUID;

public record EventDto(
        UUID eventId,
        String name,
        Integer nbPlayers
) {
}
