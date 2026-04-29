package com.group3.efreifive.recordmatch.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour représenter un événement de match à notifier à des systèmes externes
 */
public record MatchOutboundEvent(
        UUID notificationId,
        UUID matchId,
        String type,
        LocalDateTime occurredAt,
        int matchTimeMinute,
        int matchTimeSecond,
        String matchTimePeriod,
        UUID player1Id,
        UUID player2Id,
        UUID teamId,
        UUID referenceEventId,
        Boolean succeeded
) {
}
