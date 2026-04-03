package fr.efreifive.manageplayer.dto;

public record PlayerStatisticsDto(
    int matchesPlayed,
    int goalsScored,
    int assists,
    int wins
) {
}
