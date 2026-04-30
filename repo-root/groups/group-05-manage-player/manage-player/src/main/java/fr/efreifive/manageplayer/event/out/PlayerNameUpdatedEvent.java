package fr.efreifive.manageplayer.event.out;

import java.util.UUID;

public record PlayerNameUpdatedEvent(
    UUID playerId,
    String firstName,
    String lastName
) {
}
