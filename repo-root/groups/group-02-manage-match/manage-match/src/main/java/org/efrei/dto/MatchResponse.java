package org.efrei.dto;

import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;
import java.util.UUID;
import org.efrei.domain.entities.*;

@Serdeable
public record MatchResponse(
        UUID id,
        TeamResponse teamA,
        TeamResponse teamB,
        FieldResponse field,
        PeriodResponse period,
        String status
) {
    public static MatchResponse fromDomain(Match match) {
        return new MatchResponse(
                match.id().value(),
                TeamResponse.fromDomain(match.teamA()),
                TeamResponse.fromDomain(match.teamB()),
                FieldResponse.fromDomain(match.field()),
                new PeriodResponse(match.period().start(), match.period().end()),
                match.status().name()
        );
    }
}

@Serdeable
record TeamResponse(UUID id, String tag, String label) {
    public static TeamResponse fromDomain(Team team) {
        return new TeamResponse(team.id().value(), team.tag().value(), team.label().value());
    }
}

@Serdeable
record FieldResponse(UUID id, String label) {
    public static FieldResponse fromDomain(Field field) {
        return new FieldResponse(field.id().value(), field.label().value());
    }
}

@Serdeable
record PeriodResponse(LocalDateTime start, LocalDateTime end) {}