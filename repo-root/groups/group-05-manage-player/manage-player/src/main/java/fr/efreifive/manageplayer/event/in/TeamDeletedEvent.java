package fr.efreifive.manageplayer.event.in;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record TeamDeletedEvent(
    @NotNull UUID teamId
) {
}
