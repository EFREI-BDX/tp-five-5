package com.group3.efreifive.recordmatch.service;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.dto.MatchOutboundEvent;
import com.group3.efreifive.recordmatch.dto.MatchTimeDto;
import com.group3.efreifive.recordmatch.dto.PlayerDto;
import com.group3.efreifive.recordmatch.valueobject.MatchTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service de notification des événements de match à des systèmes externes
 */
@Slf4j
@Service
public class MatchEventOutboundService {

    private final RestClient restClient = RestClient.create();
    private final MatchOutboundProperties properties;
    private final IPlayerService playerService;

    public MatchEventOutboundService(final MatchOutboundProperties properties,
                                     final IPlayerService playerService) {
        this.properties = properties;
        this.playerService = playerService;
    }

    public void notifyMatchStarted(final MatchDto matchDto) {
        final MatchTimeDto matchTime = computeMatchTime(matchDto, matchDto.startedAt());
        final List<PlayerDto> team1Players = this.playerService.findByTeamId(matchDto.team1Id());
        final List<PlayerDto> team2Players = this.playerService.findByTeamId(matchDto.team2Id());
        final Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("homeTeam", buildTeamPayload(matchDto.team1Id(), team1Players));
        payload.put("awayTeam", buildTeamPayload(matchDto.team2Id(), team2Players));
        payload.put("scheduledDurationMinutes", matchDto.scheduledDurationMinutes());
        this.notify("match_started", new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), "MATCH_STARTED",
                matchDto.startedAt(), matchTime, payload));
    }

    public void notifyMatchFinished(final MatchDto matchDto, final LocalDateTime endedAt) {
        this.notify("match_finished", new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), "MATCH_FINISHED",
                endedAt, computeMatchTime(matchDto, endedAt), Map.of()));
    }

    public void notifyMatchCancelled(final MatchDto matchDto) {
        this.notify("match_cancelled", new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), "MATCH_CANCELLED",
                matchDto.startedAt(), computeMatchTime(matchDto, matchDto.startedAt()), Map.of()));
    }

    public void notifyMatchForfeited(final MatchDto matchDto, final UUID forfeitingTeamId) {
        final LocalDateTime now = LocalDateTime.now();
        this.notify("match_forfeited", new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), "MATCH_FORFEITED",
                now, computeMatchTime(matchDto, now), map("forfeitingTeamId", forfeitingTeamId)));
    }

    public void notifyMatchEvent(final MatchEventDto matchEventDto, final MatchDto matchDto, final String eventType) {
        final MatchTimeDto matchTime = computeMatchTime(matchDto, matchEventDto.occuredAt());
        final Map<String, Object> payload = buildPayload(eventType, matchEventDto);
        this.notify(eventType.toLowerCase(), new MatchOutboundEvent(
                UUID.randomUUID(), matchDto.matchId(), eventType,
                matchEventDto.occuredAt(), matchTime, payload));
    }

    private Map<String, Object> buildPayload(final String eventType, final MatchEventDto dto) {
        return switch (eventType) {
            case "GOAL_SCORED"    -> map("scoringTeamId", dto.teamId(), "scorerId", dto.player1Id());
            case "GOAL_CANCELLED" -> map("cancelledGoalEventId", dto.referenceEventId());
            case "YELLOW_CARD"    -> map("playerId", dto.player1Id());
            case "RED_CARD"       -> map("playerId", dto.player1Id(), "teamId", dto.teamId());
            case "FOUL_COMMITTED" -> map("playerId", dto.player1Id(), "teamId", dto.teamId(),
                                         "againstPlayerId", dto.player2Id());
            case "SUBSTITUTION"   -> map("playerOutId", dto.player1Id(), "playerInId", dto.player2Id());
            case "PASS_ATTEMPTED" -> map("passerId", dto.player1Id(), "teamId", dto.teamId(),
                                         "receiverId", dto.player2Id(), "succeeded", dto.succeeded());
            case "SHOT_ATTEMPTED" -> map("shooterId", dto.player1Id(), "onTarget", dto.succeeded());
            case "SAVE_MADE"      -> map("keeperId", dto.player1Id(),
                                         "relatedShotEventId", dto.referenceEventId());
            default               -> Map.of();
        };
    }

    private MatchTimeDto computeMatchTime(final MatchDto matchDto, final LocalDateTime at) {
        final MatchTime mt = MatchTime.compute(matchDto.startedAt(), matchDto.scheduledDurationMinutes(), at);
        return new MatchTimeDto(mt.minute(), mt.second(), mt.period());
    }

    private Map<String, Object> buildTeamPayload(final UUID teamId, final List<PlayerDto> players) {
        final Map<String, Object> team = new LinkedHashMap<>();
        team.put("teamId", teamId);
        team.put("startingPlayers", players.stream()
                .map(p -> Map.of("playerId", p.playerId()))
                .toList());
        return team;
    }

    private static Map<String, Object> map(final Object... keysAndValues) {
        final Map<String, Object> result = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length - 1; i += 2) {
            if (keysAndValues[i + 1] != null) {
                result.put((String) keysAndValues[i], keysAndValues[i + 1]);
            }
        }
        return result;
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
