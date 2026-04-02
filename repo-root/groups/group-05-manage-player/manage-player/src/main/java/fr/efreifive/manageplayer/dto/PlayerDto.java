package fr.efreifive.manageplayer.dto;

import java.util.List;
import java.util.UUID;

public record PlayerDto(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String gender,
    String birthDate,
    double height,
    List<UUID> teamIds
) {
}
