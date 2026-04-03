package fr.efreifive.manageplayer.dto;

import java.util.UUID;

public record CreatePlayerResponse(
    UUID id,
    String status,
    String createdAt
) {
}
