package com.group3.efreifive.recordmatch.service;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.dto.MatchOutboundEvent;
import com.group3.efreifive.recordmatch.valueobject.MatchTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

/**
 * Service de notification des événements de match à des systèmes externes
 */
@Slf4j
@Service
public class MatchEventOutboundService {

    private final RestClient restClient = RestClient.create();
    private final MatchOutboundProperties properties;

    public MatchEventOutboundService(final MatchOutboundProperties properties) {
        this.properties = properties;
    }

    public void notifyMatchStarted(final MatchDto matchDto) {
        final MatchTime matchTime = MatchTime.compute(
                matchDto.startedAt(),
                matchDto.scheduledDurationMinutes(),
                matchDto.startedAt()
        );
        final MatchOutboundEvent event = new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), "MATCH_STARTED",
                matchDto.startedAt(),
                matchTime.minute(), matchTime.second(), matchTime.period(),
                null, null, null, null, null
        );
        this.notify("match_started", event);
    }

    public void notifyMatchFinished(final MatchDto matchDto) {
        final var scheduledEnd = matchDto.startedAt().plusMinutes(matchDto.scheduledDurationMinutes());
        final MatchTime matchTime = MatchTime.compute(
                matchDto.startedAt(),
                matchDto.scheduledDurationMinutes(),
                scheduledEnd
        );
        final MatchOutboundEvent event = new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), "MATCH_FINISHED",
                scheduledEnd,
                matchTime.minute(), matchTime.second(), matchTime.period(),
                null, null, null, null, null
        );
        this.notify("match_finished", event);
    }

    public void notifyMatchCancelled(final MatchDto matchDto) {
        this.notifyMatchLifecycle("MATCH_CANCELLED", "match_cancelled", matchDto);
    }

    public void notifyMatchForfeited(final MatchDto matchDto) {
        this.notifyMatchLifecycle("MATCH_FORFEITED", "match_forfeited", matchDto);
    }

    public void notifyMatchEvent(final MatchEventDto matchEventDto, final MatchDto matchDto, final String eventType) {
        final MatchTime matchTime = MatchTime.compute(
                matchDto.startedAt(),
                matchDto.scheduledDurationMinutes(),
                matchEventDto.occuredAt()
        );
        final MatchOutboundEvent event = new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), eventType,
                matchEventDto.occuredAt(),
                matchTime.minute(), matchTime.second(), matchTime.period(),
                matchEventDto.player1Id(),
                matchEventDto.player2Id(),
                matchEventDto.teamId(),
                matchEventDto.referenceEventId(),
                matchEventDto.succeeded()
        );
        this.notify(eventType.toLowerCase(), event);
    }

    private void notifyMatchLifecycle(final String type, final String eventKey, final MatchDto matchDto) {
        final var now = matchDto.startedAt().plusMinutes(matchDto.scheduledDurationMinutes());
        final MatchTime matchTime = MatchTime.compute(matchDto.startedAt(), matchDto.scheduledDurationMinutes(), now);
        final MatchOutboundEvent event = new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), type,
                now,
                matchTime.minute(), matchTime.second(), matchTime.period(),
                null, null, null, null, null
        );
        this.notify(eventKey, event);
    }

    private void notify(final String eventType, final MatchOutboundEvent event) {
        if (this.properties.getNotifyUrls() == null) return;
        final List<String> urls = this.properties.getNotifyUrls().get(eventType);
        if (urls == null || urls.isEmpty()) return;
        for (final String url : urls) {
            try {
                this.restClient.post()
                        .uri(url)
                        .body(event)
                        .retrieve()
                        .toBodilessEntity();
                MatchEventOutboundService.log.info("Event {} sent to {}", eventType, url);
            } catch (Exception e) {
                MatchEventOutboundService.log.warn("Event {} failed to {} : {}", eventType, url, e.getMessage());
            }
        }
    }
}
