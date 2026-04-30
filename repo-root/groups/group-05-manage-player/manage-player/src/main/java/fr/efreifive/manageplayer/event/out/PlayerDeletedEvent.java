package fr.efreifive.manageplayer.event.out;

import java.util.UUID;

public record PlayerDeletedEvent(
    UUID playerId
) {
}
