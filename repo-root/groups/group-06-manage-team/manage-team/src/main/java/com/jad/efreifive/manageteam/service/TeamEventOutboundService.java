package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.valueobject.Id;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class TeamEventOutboundService {
    private final WebClient webClient = WebClient.create();

    @Value("#{${team.outbound.notify-urls}}")
    private Map<String, List<String>> notifyUrls;

    public void notifyTeamCreated(TeamDto teamDto) {
        this.notify("create", teamDto);
    }

    private void notify(String eventType, TeamDto teamDto) {
        List<String> urls = this.notifyUrls.get(eventType);
        if (urls == null) {
            return;
        }
        for (String url : urls) {
            this.webClient.post()
                    .uri(url)
                    .bodyValue(teamDto)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnSuccess(
                            response -> TeamEventOutboundService.log.info("Event {} send to {}", eventType, url))
                    .doOnError(
                            error -> TeamEventOutboundService.log.warn("Event {} failed to {} : {}", eventType,
                                                                       url, error.getMessage()))
                    .subscribe();
        }
    }

    public void notifyTeamUpdated(TeamDto teamDto) {
        this.notify("update", teamDto);
    }

    public void notifyTeamDissolve(TeamDto teamDto) {
        this.notify("dissolve", teamDto);
    }

    public void notifyTeamRestore(TeamDto teamDto) {
        this.notify("restore", teamDto);
    }

    public void notifyAssignLeader(TeamDto teamDto) {
        this.notify("assignLeader", teamDto);
    }

    public void notifyAssignPlayer(Id teamId, Id playerId) {
        TeamPlayerAssignmentEventDto event = new TeamPlayerAssignmentEventDto(teamId.value(), playerId.value());
        this.notify("assignPlayer", event);
    }

    private void notify(String eventType, Object eventPayload) {
        List<String> urls = this.notifyUrls.get(eventType);
        if (urls == null) {
            return;
        }
        for (String url : urls) {
            this.webClient.post()
                    .uri(url)
                    .bodyValue(eventPayload)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnSuccess(response -> TeamEventOutboundService.log.info("Event {} send to {}", eventType, url))
                    .doOnError(error -> TeamEventOutboundService.log.warn("Event {} failed to {} : {}", eventType, url,
                                                                          error.getMessage()))
                    .subscribe();
        }
    }

    private record TeamPlayerAssignmentEventDto(UUID teamId, UUID playerId) {
    }
}