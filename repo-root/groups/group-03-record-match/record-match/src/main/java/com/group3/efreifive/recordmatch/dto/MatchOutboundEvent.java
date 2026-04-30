package com.group3.efreifive.recordmatch.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO pour représenter un événement de match à notifier à des systèmes externes
 */
public record MatchOutboundEvent(
        UUID eventId,
        UUID matchId,
        String type,
        LocalDateTime occurredAt,
        MatchTimeDto matchTime,
        Map<String, Object> payload
) {
}
