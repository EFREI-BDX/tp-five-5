package org.efrei.five.apimanagematch.application.service;

import org.efrei.five.apimanagematch.application.inbound.TeamInboundDto;
import org.efrei.five.apimanagematch.domain.command.CommandHandler;
import org.efrei.five.apimanagematch.domain.command.dissolveteam.DissolveTeamCommand;
import org.efrei.five.apimanagematch.domain.command.syncteam.SyncTeamCommand;
import org.springframework.stereotype.Service;

@Service
public class TeamEventApplicationService {

    private final CommandHandler<SyncTeamCommand> syncTeam;
    private final CommandHandler<DissolveTeamCommand> dissolveTeam;

    public TeamEventApplicationService(CommandHandler<SyncTeamCommand> syncTeam,
                                       CommandHandler<DissolveTeamCommand> dissolveTeam) {
        this.syncTeam = syncTeam;
        this.dissolveTeam = dissolveTeam;
    }

    public void onTeamUpdated(TeamInboundDto dto) {
        syncTeam.handle(new SyncTeamCommand(dto.id(), dto.tag(), dto.label()));
    }

    public void onTeamDissolved(TeamInboundDto dto) {
        dissolveTeam.handle(new DissolveTeamCommand(dto.id()));
    }
}