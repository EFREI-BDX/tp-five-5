package fr.efreifive.manageplayer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdatePlayerStatisticsRequest(
    @NotNull @Min(0) Integer matchesPlayed,
    @NotNull @Min(0) Integer goalsScored,
    @NotNull @Min(0) Integer assists,
    @NotNull @Min(0) Integer wins
) {
}
