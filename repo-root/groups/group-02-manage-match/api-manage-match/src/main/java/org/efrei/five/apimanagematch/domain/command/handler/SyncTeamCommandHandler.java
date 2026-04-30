package org.efrei.five.apimanagematch.domain.command.handler;

import org.efrei.five.apimanagematch.domain.command.CommandHandler;
import org.efrei.five.apimanagematch.domain.command.syncteam.SyncTeamCommand;
import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.service.ITeamSyncDomainService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Label;
import org.efrei.five.apimanagematch.domain.valueobjects.Tag;
import org.springframework.stereotype.Component;

@Component
public class SyncTeamCommandHandler implements CommandHandler<SyncTeamCommand> {

    private final ITeamSyncDomainService teamSyncDomainService;

    public SyncTeamCommandHandler(ITeamSyncDomainService teamSyncDomainService) {
        this.teamSyncDomainService = teamSyncDomainService;
    }

    @Override
    public void handle(SyncTeamCommand command) {
        Team team = new Team(
                new Id(command.id()),
                new Tag(command.tag()),
                new Label(command.label())
        );
        teamSyncDomainService.syncTeam(team);
    }
}