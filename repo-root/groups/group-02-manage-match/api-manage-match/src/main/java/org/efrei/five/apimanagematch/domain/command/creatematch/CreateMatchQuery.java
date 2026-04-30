package org.efrei.five.apimanagematch.domain.command.creatematch;

import org.efrei.five.apimanagematch.domain.command.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateMatchQuery(
        UUID teamAUuid,
        UUID teamBUuid,
        UUID fieldUuid,
        LocalDateTime start,
        LocalDateTime end
) implements Query<CreateMatchResponse> {
}