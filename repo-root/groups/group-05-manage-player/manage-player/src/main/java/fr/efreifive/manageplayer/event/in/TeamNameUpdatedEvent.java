package fr.efreifive.manageplayer.event.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record TeamNameUpdatedEvent(
    @NotNull UUID teamId,
    @NotBlank @Size(max = 100) String name
) {
}
