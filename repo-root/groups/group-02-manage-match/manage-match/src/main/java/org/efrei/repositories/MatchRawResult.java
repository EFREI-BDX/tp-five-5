package org.efrei.repositories;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.MappedProperty;

import java.time.LocalDateTime;
import java.util.UUID;


@Introspected
public record MatchRawResult(
        @MappedProperty("match_uuid") UUID matchUuid,
        @MappedProperty("start_time") LocalDateTime startTime,
        @MappedProperty("end_time") LocalDateTime endTime,
        @MappedProperty("status") String status,
        @MappedProperty("field_uuid") UUID fieldUuid,
        @MappedProperty("field_label") String fieldLabel,
        @MappedProperty("team_a_uuid") UUID teamAUuid,
        @MappedProperty("team_a_tag") String teamATag,
        @MappedProperty("team_a_label") String teamALabel,
        @MappedProperty("team_b_uuid") UUID teamBUuid,
        @MappedProperty("team_b_tag") String teamBTag,
        @MappedProperty("team_b_label") String teamBLabel
) {
}