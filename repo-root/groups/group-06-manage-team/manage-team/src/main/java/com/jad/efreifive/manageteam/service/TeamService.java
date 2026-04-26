package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.controller.command.TeamCommand;
import com.jad.efreifive.manageteam.controller.command.TeamCommandResult;
import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.mapper.TeamMapper;
import com.jad.efreifive.manageteam.repository.TeamRepository;
import com.jad.efreifive.manageteam.repository.result.PersistenceOperationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    @Transactional(readOnly = true)
    public List<TeamDto> findAll() {
        TeamService.log.debug("Listing all teams");
        List<TeamDto> teams = this.teamRepository.findAll().stream().map(this.teamMapper::entityToDto).toList();
        TeamService.log.debug("Loaded {} teams", teams.size());
        return teams;
    }

    @Transactional
    public void changeName(UUID id, String newLabel) {
        TeamService.log.info("Renaming team: id={}, newLabel={}", id, newLabel);
        this.checkSuccessOrThrow(this.teamRepository.changeName(id.toString(), newLabel), "Team rename");
        TeamService.log.info("Team renamed successfully: id={}", id);
    }

    private void checkSuccessOrThrow(PersistenceOperationResult result, String operationName) {
        if (!result.success()) {
            String message = result.message();
            TeamService.log.error("{} failed: {}", operationName, message);
            throw new TeamServiceException(result.errorCode(), operationName + " failed: " + message);
        }
    }

    @Transactional
    public void changeTag(UUID id, String newTag) {
        TeamService.log.info("Changing team tag: id={}, newTag={}", id, newTag);
        this.checkSuccessOrThrow(this.teamRepository.changeTag(id.toString(), newTag), "Team change tag");
        TeamService.log.info("Team tag changed successfully: id={}", id);
    }

    @Transactional
    public TeamCommandResult executeCommand(final TeamCommand command) {
        TeamService.log.debug("Executing command: {}", command.getClass().getSimpleName());
        return switch (command) {
            case TeamCommand.TeamCreateCommand createCommand -> {
                TeamService.log.debug("Handling TeamCreateCommand: label={}, tag={}, creationDate={}",
                                      TeamCommand.getLabel(createCommand),
                                      TeamCommand.getTag(createCommand),
                                      TeamCommand.getCreationDate(createCommand));
                final UUID id = this.create(TeamCommand.getLabel(command),
                                            TeamCommand.getTag(command),
                                            TeamCommand.getCreationDate(command));
                yield TeamCommandResult.successWithPayLoad(this.findById(id));
            }

            case TeamCommand.TeamDissolveCommand dissolveCommand -> {
                TeamService.log.debug("Handling TeamDissolveCommand: id={}",
                                      TeamCommand.getId(dissolveCommand));
                yield (this.dissolve(dissolveCommand.id(), TeamCommand.getDissolutionDate(dissolveCommand)))
                        ? TeamCommandResult.successWithPayLoad(this.findById(dissolveCommand.id()))
                        : TeamCommandResult.successNoPayLoad();
            }

            case TeamCommand.TreamRestoreCommand restoreCommand -> {
                TeamService.log.debug("Handling TeamUnDissolveCommand: id={}",
                                      TeamCommand.getId(restoreCommand));
                this.restore(restoreCommand.id());
                yield TeamCommandResult.successWithPayLoad(this.findById(restoreCommand.id()));
            }
            default -> throw new UnsupportedOperationException(
                    "Not yet implemented command type: " + command.getClass().getSimpleName());
        };
    }

    @Transactional
    public UUID create(final String label, final String tag, final LocalDate creationDate) {
        TeamService.log.info("Creating team: label={}, tag={}", label, tag);
        final UUID id = UUID.randomUUID();
        final PersistenceOperationResult result = this.teamRepository.create(id.toString(), label, tag, creationDate);
        this.checkSuccessOrThrow(result, "Team create");
        TeamService.log.info("Team created successfully: id={}", id);
        return id;
    }

    @Transactional(readOnly = true)
    public TeamDto findById(UUID id) {
        TeamService.log.debug("Looking up team with id={}", id);

        return this.teamRepository.findById(id.toString())
                .map(entity -> {
                    TeamService.log.debug("Team found: {}", entity);
                    return this.teamMapper.entityToDto(entity);
                })
                .orElseThrow(() -> {
                    TeamService.log.warn("Team not found with id={}", id);
                    return new TeamNotFoundException("Team not found: " + id);
                });
    }

    @Transactional
    public boolean dissolve(UUID id, LocalDate dissolutionDate) {
        TeamService.log.info("Dissolving team: id={}, dissolutionDate={}", id, dissolutionDate);
        final PersistenceOperationResult result = this.teamRepository.dissolve(id.toString(), dissolutionDate);
        this.checkSuccessOrThrow(result, "Team dissolve");
        TeamService.log.info("Team dissolved successfully: id={}", id);
        return PersistenceOperationResult.getResult(result);
    }

    @Transactional
    public void restore(UUID id) {
        TeamService.log.info("Restoring team: id={}", id);
        this.checkSuccessOrThrow(this.teamRepository.restore(id.toString()), "Team restore");
        TeamService.log.info("Team restored successfully: id={}", id);
    }

}

