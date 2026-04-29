package com.group3.efreifive.recordmatch.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchDto(
        UUID matchId,
        UUID team1Id,
        UUID team2Id,
        LocalDateTime startedAt,
        Integer scheduledDurationMinutes
) {
}
