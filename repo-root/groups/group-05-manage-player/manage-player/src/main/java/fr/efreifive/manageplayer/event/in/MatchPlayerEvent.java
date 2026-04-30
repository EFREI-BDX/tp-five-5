package fr.efreifive.manageplayer.event.in;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MatchPlayerEvent(
    @NotNull UUID playerId,
    @NotNull @Min(0) Integer matchesPlayed,
    @NotNull @Min(0) Integer goalsScored,
    @NotNull @Min(0) Integer assists,
    @NotNull @Min(0) Integer wins,
    @NotNull @Min(0) Integer losses,
    @NotNull @Min(0) Integer draws,
    @NotNull @Min(0) Integer mvps
) {
}
