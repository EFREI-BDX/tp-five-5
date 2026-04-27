package org.efrei.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDateTime;
import java.util.UUID;

@Serdeable
public record CreateMatchRequest(
        UUID teamAUuid,
        UUID teamBUuid,
        UUID fieldUuid,
        LocalDateTime start,
        LocalDateTime end
) {}