package org.efrei.five.apimanagematch.external.dto;

public record StartMatchRequest(
        java.util.UUID matchId,
        java.util.UUID team1Id,
        java.util.UUID team2Id,
        java.time.LocalDateTime startedAt,
        long duration
) {
}
