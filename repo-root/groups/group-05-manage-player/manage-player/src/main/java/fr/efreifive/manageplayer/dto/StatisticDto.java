package fr.efreifive.manageplayer.dto;

import java.util.UUID;

public record StatisticDto(
    UUID playerId,
    int matchesPlayed,
    int goalsScored,
    int assists,
    int wins,
    int draws,
    int mvps
) {
}
