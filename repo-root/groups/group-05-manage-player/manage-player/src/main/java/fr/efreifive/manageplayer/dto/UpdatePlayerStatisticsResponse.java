package fr.efreifive.manageplayer.dto;

import java.util.UUID;

public record UpdatePlayerStatisticsResponse(
    UUID id,
    PlayerStatisticsDto statistics,
    String updatedAt
) {
}
