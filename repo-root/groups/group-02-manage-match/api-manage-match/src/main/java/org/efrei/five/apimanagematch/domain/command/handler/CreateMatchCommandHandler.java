package org.efrei.five.apimanagematch.domain.command.handler;

import org.efrei.five.apimanagematch.domain.command.QueryHandler;
import org.efrei.five.apimanagematch.domain.command.creatematch.CreateMatchQuery;
import org.efrei.five.apimanagematch.domain.command.creatematch.CreateMatchResponse;
import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.mapper.MatchMapper;
import org.efrei.five.apimanagematch.domain.service.IMatchDomainService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.springframework.stereotype.Component;

@Component
public class CreateMatchCommandHandler
        implements QueryHandler<CreateMatchQuery, CreateMatchResponse> {


    private final IMatchDomainService service;

    public CreateMatchCommandHandler(IMatchDomainService service) {
        this.service = service;
    }


    @Override
    public CreateMatchResponse handle(CreateMatchQuery command) {

        Match match = service.createMatch(
                new Id(command.teamAUuid()),
                new Id(command.teamBUuid()),
                new Id(command.fieldUuid()),
                new Period(command.start(), command.end())
        );

        return new CreateMatchResponse(MatchMapper.toDto(match));
    }
}