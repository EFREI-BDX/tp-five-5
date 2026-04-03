package fr.efreifive.manageplayer.dto;

import java.util.UUID;

public record UpdatePlayerResponse(
    UUID id,
    String updatedAt
) {
}
