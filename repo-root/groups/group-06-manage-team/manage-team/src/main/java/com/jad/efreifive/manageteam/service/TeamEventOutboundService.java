package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.dto.TeamDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
public class TeamEventOutboundService {
    private final WebClient webClient = WebClient.create();
    private final TeamOutboundProperties properties;

    public TeamEventOutboundService(TeamOutboundProperties properties) {
        this.properties = properties;
    }

    public void notifyTeamCreated(TeamDto teamDto) {
        this.notify("create", teamDto);
    }

    private <T> void notify(String eventType, T dto) {
        List<String> urls = this.properties.getNotifyUrls().get(eventType);
        if (urls == null) return;
        for (String url : urls) {
            this.webClient.post()
                    .uri(url)
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .doOnSuccess(
                            _ -> TeamEventOutboundService.log.info("Event {} send to {}", eventType, url))
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
        this.notify("assign_leader", teamDto);
    }

    public void notifyAssignPlayer(PlayerDto playerDto) {
        this.notify("assign_member", playerDto);
    }

    public void notifyUnassignPlayer(PlayerDto playerDto) {
        this.notify("remove_member", playerDto);
    }
}