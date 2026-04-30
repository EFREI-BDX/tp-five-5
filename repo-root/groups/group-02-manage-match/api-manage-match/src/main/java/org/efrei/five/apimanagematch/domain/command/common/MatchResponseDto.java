package org.efrei.five.apimanagematch.domain.command.common;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchResponseDto(
        UUID id,
        TeamResponse teamA,
        TeamResponse teamB,
        FieldResponse field,
        PeriodResponse period,
        String status
) {
    public record TeamResponse(UUID id, String tag, String label) {
    }

    public record PeriodResponse(LocalDateTime start, LocalDateTime end) {
    }

    public record FieldResponse(UUID id, String label) {
    }
}
