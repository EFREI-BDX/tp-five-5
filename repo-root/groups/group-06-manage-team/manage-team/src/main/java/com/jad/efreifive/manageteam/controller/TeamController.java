package com.jad.efreifive.manageteam.controller;

import com.jad.efreifive.manageteam.controller.command.TeamCommand;
import com.jad.efreifive.manageteam.controller.command.TeamCommandResult;
import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.service.ITeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final ITeamService teamService;

    public TeamController(ITeamService teamService) {
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
        TeamCommandResult result = this.teamService.executeCommand(new TeamCommand.TeamCreateCommand(teamDto));
        return switch (result) {
            case TeamCommandResult.SuccessWithTeamDtoPayLoad _ -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(TeamCommandResult.getTeamDtoPayload(result));
            case TeamCommandResult.SuccessNoPayLoad _ -> ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(null);
            case TeamCommandResult.Failure failure -> ResponseEntity
                    .status(DomainErrorCodeHttpStatusMapper.fromDomainErrorCode(failure.domainErrorCode()))
                    .body(null);
            default -> throw new IllegalStateException("Unexpected result type: " + result.getClass());
        };
    }

    @PutMapping("/{id}/label")
    public ResponseEntity<TeamDto> changeLabel(@PathVariable("id") UUID id, @RequestBody String newLabel) {
        TeamCommandResult result = this.teamService.executeCommand(
                new TeamCommand.TeamUpdateLabelCommand(id, newLabel));
        return this.toTeamDtoResponseEntity(result);
    }

    @NonNull
    private ResponseEntity<TeamDto> toTeamDtoResponseEntity(final TeamCommandResult result) {
        return switch (result) {
            case TeamCommandResult.SuccessWithTeamDtoPayLoad _ -> ResponseEntity
                    .status(HttpStatus.OK)
                    .body(TeamCommandResult.getTeamDtoPayload(result));
            case TeamCommandResult.SuccessNoPayLoad _ -> ResponseEntity
                    .status(HttpStatus.OK)
                    .body(null);
            case TeamCommandResult.Failure failure -> ResponseEntity
                    .status(DomainErrorCodeHttpStatusMapper.fromDomainErrorCode(failure.domainErrorCode()))
                    .body(null);
            default -> throw new IllegalStateException("Unexpected result type: " + result.getClass());
        };
    }

    @PutMapping("/{id}/tag")
    public ResponseEntity<TeamDto> changeTag(@PathVariable UUID id, @RequestBody String newTag) {
        TeamCommandResult result = this.teamService.executeCommand(
                new TeamCommand.TeamUpdateTagCommand(id, newTag));
        return this.toTeamDtoResponseEntity(result);
    }

    @PutMapping("/{id}/dissolve")
    public ResponseEntity<TeamDto> dissolve(@PathVariable UUID id, @RequestBody LocalDate dissolutionDate) {
        TeamCommandResult result = this.teamService.executeCommand(
                new TeamCommand.TeamDissolveCommand(id, dissolutionDate));
        return this.toTeamDtoResponseEntity(result);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<TeamDto> restore(@PathVariable UUID id) {
        TeamCommandResult result = this.teamService.executeCommand(
                new TeamCommand.TeamRestoreCommand(id));
        return this.toTeamDtoResponseEntity(result);
    }

    @PutMapping("/{id}/team-leader")
    public ResponseEntity<TeamDto> changeTeamLeader(@PathVariable UUID id, @RequestBody UUID newTeamLeaderId) {
        TeamCommandResult result = this.teamService.executeCommand(
                new TeamCommand.TeamUpdateTeamLeaderCommand(id, newTeamLeaderId));
        return this.toTeamDtoResponseEntity(result);
    }

    @PutMapping("/{teamId}/assign-player")
    public ResponseEntity<PlayerDto> assignPlayer(@PathVariable UUID teamId, @RequestBody UUID playerId) {
        TeamCommandResult result = this.teamService.executeCommand(
                new TeamCommand.TeamAssignPlayerCommand(teamId, playerId));
        return this.toPlayerDtoResponseEntity(result);
    }

    @NonNull
    private ResponseEntity<PlayerDto> toPlayerDtoResponseEntity(final TeamCommandResult result) {
        return switch (result) {
            case TeamCommandResult.SuccessWithPlayerDtoPayLoad _ -> ResponseEntity
                    .status(HttpStatus.OK)
                    .body(TeamCommandResult.getPlayerDtoPayload(result));
            case TeamCommandResult.SuccessNoPayLoad _ -> ResponseEntity
                    .status(HttpStatus.OK)
                    .body(null);
            case TeamCommandResult.Failure failure -> ResponseEntity
                    .status(DomainErrorCodeHttpStatusMapper.fromDomainErrorCode(failure.domainErrorCode()))
                    .body(null);
            default -> throw new IllegalStateException("Unexpected result type: " + result.getClass());
        };
    }

    @PutMapping("/{teamId}/remove-player")
    public ResponseEntity<PlayerDto> removePlayer(@PathVariable UUID teamId, @RequestBody UUID playerId) {
        TeamCommandResult result = this.teamService.executeCommand(
                new TeamCommand.TeamRemovePlayerCommand(teamId, playerId));
        return this.toPlayerDtoResponseEntity(result);
    }
}

