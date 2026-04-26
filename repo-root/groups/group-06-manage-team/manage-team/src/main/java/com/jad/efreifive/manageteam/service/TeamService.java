package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.controller.command.TeamCommand;
import com.jad.efreifive.manageteam.controller.command.TeamCommandResult;
import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.mapper.TeamMapper;
import com.jad.efreifive.manageteam.repository.TeamRepository;
import com.jad.efreifive.manageteam.repository.result.PersistenceOperationResult;
import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Label;
import com.jad.efreifive.manageteam.valueobject.Period;
import com.jad.efreifive.manageteam.valueobject.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
class TeamService implements ITeamService, ITeamServiceForTest {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final TeamEventOutboundService teamEventOutboundService;
    private final PlayerService playerService;

    public TeamService(final TeamRepository teamRepository,
                       final TeamMapper teamMapper,
                       final TeamEventOutboundService teamEventOutboundService, final PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.teamEventOutboundService = teamEventOutboundService;
        this.playerService = playerService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamDto> findAll() {
        TeamService.log.debug("Listing all teams");
        List<TeamDto> teams = this.teamRepository.findAll().stream().map(this.teamMapper::entityToDto).toList();
        TeamService.log.debug("Loaded {} teams", teams.size());
        return teams;
    }

    @Transactional(readOnly = true)
    @Override
    public TeamDto findById(UUID id) {
        TeamService.log.debug("Looking up team with id={}", id);

        return this.teamRepository.findById(id.toString())
                .map(entity -> {
                    TeamService.log.debug("Team found: {}", entity);
                    return this.teamMapper.entityToDto(entity);
                })
                .orElseThrow(() -> {
                    TeamService.log.warn("Team not found with id={}", id);
                    return new TeamServiceException(DomainErrorCode.TEAM_NOT_FOUND, "Team not found: " + id);
                });
    }

    @Transactional
    @Override
    public TeamCommandResult executeCommand(final TeamCommand command) {
        TeamService.log.debug("Executing command: {}", command.toLogString());
        return switch (command) {

            case TeamCommand.TeamCreateCommand createCommand -> {
                final Id id = this.create(new Label(TeamCommand.getLabel(createCommand)),
                                          new Tag(TeamCommand.getTag(createCommand)),
                                          new Period(TeamCommand.getCreationDate(createCommand), null));
                yield TeamCommandResult.successWithPayLoad(this.findById(id));
            }

            case TeamCommand.TeamUpdateLabelCommand updateLabelCommand -> {
                this.changeName(new Id(updateLabelCommand.id()), new Label(updateLabelCommand.newLabel()));
                yield TeamCommandResult.successWithPayLoad(this.findById(updateLabelCommand.id()));
            }

            case TeamCommand.TeamUpdateTagCommand updateTagCommand -> {
                this.changeTag(new Id(updateTagCommand.id()), new Tag(updateTagCommand.newTag()));
                yield TeamCommandResult.successWithPayLoad(this.findById(updateTagCommand.id()));
            }

            case TeamCommand.TeamDissolveCommand dissolveCommand -> {
                if (this.dissolve(new Id(dissolveCommand.id()),
                                  new Period(TeamCommand.getDissolutionDate(dissolveCommand),
                                             TeamCommand.getDissolutionDate(dissolveCommand)))) {
                    yield TeamCommandResult.successWithPayLoad(this.findById(dissolveCommand.id()));
                }
                yield TeamCommandResult.successNoPayLoad();
            }

            case TeamCommand.TeamRestoreCommand restoreCommand -> {
                this.restore(new Id(restoreCommand.id()));
                yield TeamCommandResult.successWithPayLoad(this.findById(restoreCommand.id()));
            }

            case TeamCommand.TeamUpdateTeamLeaderCommand teamUpdateTeamLeaderCommand -> {
                this.updateTeamLeader(new Id(teamUpdateTeamLeaderCommand.id()),
                                      new Id(teamUpdateTeamLeaderCommand.playerId()));
                yield TeamCommandResult.successWithPayLoad(this.findById(teamUpdateTeamLeaderCommand.id()));
            }

            case TeamCommand.TeamAssignPlayerCommand teamAssignPlayerCommand -> {
                this.assignTeam(new Id(teamAssignPlayerCommand.id()),
                                new Id(teamAssignPlayerCommand.playerId()));
                yield TeamCommandResult.successWithPayLoad(
                        this.playerService.findById(teamAssignPlayerCommand.playerId()));
            }

            case TeamCommand.TeamRemovePlayerCommand teamRemovePlayerCommand -> {
                this.removePlayer(new Id(teamRemovePlayerCommand.id()),
                                  new Id(teamRemovePlayerCommand.playerId()));
                yield TeamCommandResult.successWithPayLoad(
                        this.playerService.findById(teamRemovePlayerCommand.playerId()));
            }
        };
    }

    @Transactional
    @Override
    public Id create(final Label label, final Tag tag, final Period period) {
        final Id id = Id.newId();
        final PersistenceOperationResult result = this.teamRepository.create(id, label, tag, period);
        this.checkSuccessOrThrow(result, "Team create");
        TeamService.log.info("Team created successfully: id={}", id.value());
        this.teamEventOutboundService.notifyTeamCreated(this.findById(id));
        return id;
    }

    @Transactional(readOnly = true)
    TeamDto findById(Id id) {
        return this.findById(id.value());
    }

    @Transactional
    @Override
    public void changeName(final Id id, final Label newLabel) {
        this.checkSuccessOrThrow(this.teamRepository.changeName(id, newLabel), "Team rename");
        TeamService.log.info("Team renamed successfully: id={}", id);
        this.teamEventOutboundService.notifyTeamUpdated(this.findById(id));
    }

    @Transactional
    @Override
    public void changeTag(final Id id, final Tag newTag) {
        this.checkSuccessOrThrow(this.teamRepository.changeTag(id, newTag), "Team change tag");
        TeamService.log.info("Team tag changed successfully: id={}", id);
        this.teamEventOutboundService.notifyTeamUpdated(this.findById(id));
    }

    @Transactional
    @Override
    public boolean dissolve(final Id id, Period period) {
        final PersistenceOperationResult result = this.teamRepository.dissolve(id, period);
        this.checkSuccessOrThrow(result, "Team dissolve");
        TeamService.log.info("Team dissolved successfully: id={}", id);
        this.teamEventOutboundService.notifyTeamDissolve(this.findById(id));
        return PersistenceOperationResult.getResult(result);
    }

    @Transactional
    @Override
    public void restore(final Id id) {
        this.checkSuccessOrThrow(this.teamRepository.restore(id), "Team restore");
        TeamService.log.info("Team restored successfully: id={}", id);
        this.teamEventOutboundService.notifyTeamRestore(this.findById(id));
    }

    @Transactional
    void updateTeamLeader(final Id id, final Id idTeamLeader) {
        this.checkSuccessOrThrow(this.teamRepository.changeTeamLeader(id, idTeamLeader),
                                 "Team change team leader");
        TeamService.log.info("Team leader changed successfully: teamId={}", id);
        this.teamEventOutboundService.notifyTeamUpdated(this.findById(id));
    }

    private void assignTeam(final Id id, final Id idPlayer) {
        this.playerService.assignTeam(idPlayer, id);
        this.teamEventOutboundService.notifyAssignPlayer(this.playerService.findById(idPlayer));
    }

    private void removePlayer(final Id id, final Id idPlayer) {
        this.playerService.unassignTeam(idPlayer);
        this.teamEventOutboundService.notifyUnassignPlayer(this.playerService.findById(idPlayer));
    }

    private void checkSuccessOrThrow(PersistenceOperationResult result, String operationName) {
        if (!result.success()) {
            String message = result.message();
            TeamService.log.error("{} failed: {}", operationName, message);
            throw new TeamServiceException(result.errorCode(), operationName + " failed: " + message);
        }
    }
}

