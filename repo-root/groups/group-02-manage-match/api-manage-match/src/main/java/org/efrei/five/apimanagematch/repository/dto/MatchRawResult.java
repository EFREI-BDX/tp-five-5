package org.efrei.five.apimanagematch.repository.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchRawResult(
        UUID matchUuid,
        UUID teamAUuid,
        String teamATag,
        String teamALabel,
        UUID teamBUuid,
        String teamBTag,
        String teamBLabel,
        UUID fieldUuid,
        String fieldLabel,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String status
) {
}