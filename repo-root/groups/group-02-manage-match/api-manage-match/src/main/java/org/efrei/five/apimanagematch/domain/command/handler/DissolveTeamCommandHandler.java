package org.efrei.five.apimanagematch.domain.command.handler;

import org.efrei.five.apimanagematch.domain.command.CommandHandler;
import org.efrei.five.apimanagematch.domain.command.dissolveteam.DissolveTeamCommand;
import org.efrei.five.apimanagematch.domain.service.IMatchCancellationDomainService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.stereotype.Component;

@Component
public class DissolveTeamCommandHandler implements CommandHandler<DissolveTeamCommand> {

    private final IMatchCancellationDomainService matchCancellationDomainService;

    public DissolveTeamCommandHandler(IMatchCancellationDomainService matchCancellationDomainService) {
        this.matchCancellationDomainService = matchCancellationDomainService;
    }

    @Override
    public void handle(DissolveTeamCommand command) {
        matchCancellationDomainService.cancelMatchesForTeam(new Id(command.teamId()));
    }
}