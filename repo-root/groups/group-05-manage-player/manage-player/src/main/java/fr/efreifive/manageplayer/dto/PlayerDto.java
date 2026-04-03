package fr.efreifive.manageplayer.dto;

import java.util.List;
import java.util.UUID;

public record PlayerDto(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String birthDate,
    String gender,
    double height,
    String status,
    PlayerStatisticsDto statistics,
    List<String> teamIds,
    String createdAt,
    String updatedAt
) {
}
