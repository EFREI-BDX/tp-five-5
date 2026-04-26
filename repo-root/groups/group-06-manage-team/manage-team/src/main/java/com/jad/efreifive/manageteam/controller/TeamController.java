package com.jad.efreifive.manageteam.controller;

import com.jad.efreifive.manageteam.controller.command.TeamCommand;
import com.jad.efreifive.manageteam.controller.command.TeamCommandResult;
import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> findAll() {
        return ResponseEntity.ok(this.teamService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.teamService.findById(id));
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<TeamDto> create(@RequestBody TeamDto teamDto) {
        TeamCommandResult teamCommandResult = this.teamService.executeCommand(
                new TeamCommand.TeamCreateCommand(teamDto));
        return switch (teamCommandResult) {
            case TeamCommandResult.SuccessWithPayLoad success -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(TeamCommandResult.getPayload(teamCommandResult));
            case TeamCommandResult.SuccessNoPayLoad success -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(null);
            case TeamCommandResult.Failure failure -> ResponseEntity
                    .status(DomainErrorCodeHttpStatusMapper.fromDomainErrorCode(failure.domainErrorCode()))
                    .body(null);
        };
    }

    @PutMapping("/{id}/label")
    public ResponseEntity<TeamDto> changeLabel(@PathVariable UUID id, @RequestBody String newLabel) {
        TeamCommandResult teamCommandResult = this.teamService.executeCommand(
                new TeamCommand.TeamUpdateLabelCommand(id, newLabel));
        return switch (teamCommandResult) {
            case TeamCommandResult.SuccessWithPayLoad success -> ResponseEntity
                    .status(HttpStatus.OK)
                    .body(TeamCommandResult.getPayload(teamCommandResult));
            case TeamCommandResult.SuccessNoPayLoad success -> ResponseEntity
                    .status(HttpStatus.OK)
                    .body(null);
            case TeamCommandResult.Failure failure -> ResponseEntity
                    .status(DomainErrorCodeHttpStatusMapper.fromDomainErrorCode(failure.domainErrorCode()))
                    .body(null);
        };
    }

}

