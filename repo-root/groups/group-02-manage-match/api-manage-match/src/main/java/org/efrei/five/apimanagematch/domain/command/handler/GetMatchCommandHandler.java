package org.efrei.five.apimanagematch.domain.command.handler;


import org.efrei.five.apimanagematch.domain.command.QueryHandler;
import org.efrei.five.apimanagematch.domain.command.getmatch.GetMatchQuery;
import org.efrei.five.apimanagematch.domain.command.getmatch.GetMatchResponse;
import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.mapper.MatchMapper;
import org.efrei.five.apimanagematch.domain.service.IMatchDomainService;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class GetMatchCommandHandler implements QueryHandler<GetMatchQuery, GetMatchResponse> {

    private final IMatchDomainService service;

    GetMatchCommandHandler(IMatchDomainService matchDomainService) {
        this.service = matchDomainService;
    }

    @Override
    public GetMatchResponse handle(GetMatchQuery command) {

        Optional<Match> match = service.getMatch(new Id(command.id()));

        return new GetMatchResponse(match.map(MatchMapper::toDto));
    }

}


