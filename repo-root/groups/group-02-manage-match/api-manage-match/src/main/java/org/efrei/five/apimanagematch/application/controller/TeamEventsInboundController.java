package org.efrei.five.apimanagematch.application.controller;

import org.efrei.five.apimanagematch.application.inbound.TeamInboundDto;
import org.efrei.five.apimanagematch.application.service.TeamEventApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inbounds/teams")
public class TeamEventsInboundController {

    private final TeamEventApplicationService teamEventApplicationService;

    public TeamEventsInboundController(TeamEventApplicationService teamEventApplicationService) {
        this.teamEventApplicationService = teamEventApplicationService;
    }

    @PostMapping("/updated")
    public ResponseEntity<Void> teamUpdated(@RequestBody TeamInboundDto dto) {
        teamEventApplicationService.onTeamUpdated(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dissolved")
    public ResponseEntity<Void> teamDissolved(@RequestBody TeamInboundDto dto) {
        teamEventApplicationService.onTeamDissolved(dto);
        return ResponseEntity.ok().build();
    }
}