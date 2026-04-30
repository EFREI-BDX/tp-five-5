package org.efrei.five.apimanagematch.application.inbound;

import java.time.LocalDate;
import java.util.UUID;

public record TeamInboundDto(
        UUID id,
        String label,
        String tag,
        LocalDate creationDate,
        LocalDate dissolutionDate,
        UUID idTeamLeader,
        String state
) {
}