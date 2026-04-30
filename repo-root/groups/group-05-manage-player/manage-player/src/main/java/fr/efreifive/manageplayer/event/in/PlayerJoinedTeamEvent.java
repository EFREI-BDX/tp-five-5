package fr.efreifive.manageplayer.event.in;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PlayerJoinedTeamEvent(
    @NotNull UUID playerId,
    @NotNull UUID teamId
) {
}
