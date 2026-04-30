package fr.efreifive.manageplayer.event.out;

import java.util.UUID;

public record PlayerCreatedEvent(
    UUID playerId,
    String firstName,
    String lastName,
    String email,
    String status
) {
}
