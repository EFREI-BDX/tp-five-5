package com.group3.efreifive.recordmatch.dto;

import java.util.UUID;

public record MatchDto(
        UUID matchId,
        UUID team1Id,
        UUID team2Id
) {
}
