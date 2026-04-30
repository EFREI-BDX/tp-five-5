package org.efrei.five.apimanagematch.application.controller;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchRequest(
        UUID teamAUuid, UUID
teamBUuid,
        UUID fieldUuid, LocalDateTime
        start,
        LocalDateTime end
) {
}
