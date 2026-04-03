package fr.efreifive.manageplayer.dto;

import java.util.UUID;

public record DeletePlayerResponse(
    UUID id,
    String status,
    String updatedAt
) {
}
