package org.efrei.five.apimanagematch.domain.command.handler;

import org.efrei.five.apimanagematch.domain.command.CommandHandler;
import org.efrei.five.apimanagematch.domain.command.cancelmatch.CancelMatchCommand;
import org.efrei.five.apimanagematch.domain.service.IMatchDomainService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.stereotype.Component;

@Component
public class CancelMatchCommandHandler implements CommandHandler<CancelMatchCommand> {

    private final IMatchDomainService service;

    CancelMatchCommandHandler(IMatchDomainService service) {
        this.service = service;
    }

    @Override
    public void handle(CancelMatchCommand command) {
        service.cancelMatch(new Id(command.id()));
    }
}
