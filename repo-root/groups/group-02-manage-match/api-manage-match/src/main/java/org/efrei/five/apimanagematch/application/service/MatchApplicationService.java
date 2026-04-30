package org.efrei.five.apimanagematch.application.service;

import org.efrei.five.apimanagematch.application.controller.MatchRequest;
import org.efrei.five.apimanagematch.domain.command.CommandHandler;
import org.efrei.five.apimanagematch.domain.command.QueryHandler;
import org.efrei.five.apimanagematch.domain.command.cancelmatch.CancelMatchCommand;
import org.efrei.five.apimanagematch.domain.command.common.MatchResponseDto;
import org.efrei.five.apimanagematch.domain.command.creatematch.CreateMatchQuery;
import org.efrei.five.apimanagematch.domain.command.creatematch.CreateMatchResponse;
import org.efrei.five.apimanagematch.domain.command.getmatch.GetMatchQuery;
import org.efrei.five.apimanagematch.domain.command.getmatch.GetMatchResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MatchApplicationService {

    private final QueryHandler<GetMatchQuery, GetMatchResponse> getMatch;
    private final CommandHandler<CancelMatchCommand> cancelMatch;
    private final QueryHandler<CreateMatchQuery, CreateMatchResponse> createMatch;

    public MatchApplicationService(QueryHandler<GetMatchQuery, GetMatchResponse> getMatch, CommandHandler<CancelMatchCommand> cancelMatch, QueryHandler<CreateMatchQuery, CreateMatchResponse> createMatch) {
        this.getMatch = getMatch;
        this.cancelMatch = cancelMatch;
        this.createMatch = createMatch;
    }


    public Optional<MatchResponseDto> getMatch(UUID id) {
        GetMatchQuery command = new GetMatchQuery(id);
        GetMatchResponse response = getMatch.handle(command);
        return response.match();
    }

    public void cancelMatch(UUID id) {
        cancelMatch.handle(new CancelMatchCommand(id));
    }


    public MatchResponseDto create(MatchRequest request) {
        CreateMatchQuery command = new CreateMatchQuery(request.teamAUuid(), request.teamBUuid(), request.fieldUuid(), request.start(), request.end());
        CreateMatchResponse response = createMatch.handle(command);
        return response.match();
    }

}
