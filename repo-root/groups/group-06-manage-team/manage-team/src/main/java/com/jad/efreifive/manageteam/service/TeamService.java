package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.command.CommandResult;
import com.jad.efreifive.manageteam.command.team.TeamCommand;
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
        this.assertSuccess(this.teamRepository.changeName(id.toString(), newLabel), "Team rename failed");
        TeamService.log.info("Team renamed successfully: id={}", id);
    }

    private void assertSuccess(PersistenceOperationResult result, String defaultMessage) {
        if (!result.success()) {
            String msg = result.message() == null || result.message().isBlank() ? defaultMessage : result.message();
            TeamService.log.error("Operation failed: {}", msg);
            throw new ServiceOperationException(msg);
        }
    }

    @Transactional
    public void changeTag(UUID id, String newTag) {
        TeamService.log.info("Changing team tag: id={}, newTag={}", id, newTag);
        this.assertSuccess(this.teamRepository.changeTag(id.toString(), newTag), "Team change tag failed");
        TeamService.log.info("Team tag changed successfully: id={}", id);
    }

    @Transactional
    public CommandResult<TeamDto> executeCommand(final TeamCommand command) {
        TeamService.log.debug("Executing command: {}", command.getClass().getSimpleName());
        return switch (command) {
            case TeamCommand.TeamCreateCommand createCommand -> {
                TeamService.log.debug("Handling TeamCreateCommand: label={}, tag={}, creationDate={}",
                                      TeamCommand.getLabel(createCommand),
                                      TeamCommand.getTag(createCommand),
                                      TeamCommand.getCreationDate(createCommand));
                final UUID id = this.handleCreateCommand(createCommand);
                yield CommandResult.withPayLoad(this.findById(id));
            }

            case TeamCommand.TeamDissolveCommand dissolveCommand -> {
                TeamService.log.debug("Handling TeamDissolveCommand: id={}",
                                      TeamCommand.getId(dissolveCommand));
                yield (this.handleDissolveCommand(dissolveCommand))
                        ? CommandResult.withPayLoad(this.findById(dissolveCommand.id()))
                        : CommandResult.noPayLoad();
            }

            case TeamCommand.TreamRestoreCommand restoreCommand -> {
                TeamService.log.debug("Handling TeamUnDissolveCommand: id={}",
                                      TeamCommand.getId(restoreCommand));
                this.restore(restoreCommand.id());
                yield CommandResult.withPayLoad(this.findById(restoreCommand.id()));
            }
            default -> throw new UnsupportedOperationException(
                    "Not yet implemented command type: " + command.getClass().getSimpleName());
        };
    }

    private UUID handleCreateCommand(final TeamCommand.TeamCreateCommand command) {
        return this.create(TeamCommand.getLabel(command),
                           TeamCommand.getTag(command),
                           TeamCommand.getCreationDate(command));
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
                    return new ResourceNotFoundException("Team not found: " + id);
                });
    }

    private boolean handleDissolveCommand(final TeamCommand.TeamDissolveCommand dissolveCommand) {
        return this.dissolve(dissolveCommand.id(), TeamCommand.getDissolutionDate(dissolveCommand));
    }

    @Transactional
    public void restore(UUID id) {
        TeamService.log.info("Restoring team: id={}", id);
        this.assertSuccess(this.teamRepository.restore(id.toString()), "Team restore failed");
        TeamService.log.info("Team restored successfully: id={}", id);
    }

    @Transactional
    public UUID create(final String label, final String tag, final LocalDate creationDate) {
        TeamService.log.info("Creating team: label={}, tag={}", label, tag);
        final UUID id = UUID.randomUUID();
        final PersistenceOperationResult result = this.teamRepository.create(id.toString(), label, tag, creationDate);
        this.assertSuccess(result, "Team create failed");
        TeamService.log.info("Team created successfully: id={}", id);
        return id;
    }

    @Transactional
    public boolean dissolve(UUID id, LocalDate dissolutionDate) {
        TeamService.log.info("Dissolving team: id={}, dissolutionDate={}", id, dissolutionDate);
        final PersistenceOperationResult result = this.teamRepository.dissolve(id.toString(), dissolutionDate);
        this.assertSuccess(result, "Team dissolve failed");
        TeamService.log.info("Team dissolved successfully: id={}", id);
        return PersistenceOperationResult.getResult(result);
    }
}

