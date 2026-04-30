package com.group3.efreifive.recordmatch.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EndMatchDto(UUID matchId, LocalDateTime endedAt) {
}
