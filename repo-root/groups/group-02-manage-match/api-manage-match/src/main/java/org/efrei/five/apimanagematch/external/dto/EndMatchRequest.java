package org.efrei.five.apimanagematch.external.dto;

public record EndMatchRequest(
        java.util.UUID matchId,
        java.time.LocalDateTime endedAt
) {
}
