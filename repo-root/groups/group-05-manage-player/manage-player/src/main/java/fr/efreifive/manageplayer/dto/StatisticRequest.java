package fr.efreifive.manageplayer.dto;

public record StatisticRequest(
    Integer matchesPlayed,
    Integer goalsScored,
    Integer assists,
    Integer wins,
    Integer draws,
    Integer mvps
) {
}
