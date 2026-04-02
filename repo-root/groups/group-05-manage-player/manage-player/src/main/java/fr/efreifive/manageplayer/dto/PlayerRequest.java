package fr.efreifive.manageplayer.dto;

import java.util.List;
import java.util.UUID;

public record PlayerRequest(
    String firstName,
    String lastName,
    String email,
    String phone,
    String gender,
    String birthDate,
    Double height,
    List<UUID> teamIds
) {
}
