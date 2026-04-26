package com.jad.efreifive.manageteam.controller;

import com.jad.efreifive.manageteam.command.CommandResult;
import com.jad.efreifive.manageteam.command.team.TeamCommand;
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
        CommandResult<TeamDto> commandResult = this.teamService.executeCommand(
                new TeamCommand.TeamCreateCommand(teamDto));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandResult.getPayload("Create must return a payload."));
    }
}

