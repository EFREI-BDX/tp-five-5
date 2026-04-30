package org.efrei.five.apimanagematch.domain.service;


import org.efrei.five.apimanagematch.domain.entities.Field;
import org.efrei.five.apimanagematch.domain.entities.Match;
import org.efrei.five.apimanagematch.domain.entities.Team;
import org.efrei.five.apimanagematch.domain.exception.ImpossibleReservationException;
import org.efrei.five.apimanagematch.domain.exception.NotFoundException;
import org.efrei.five.apimanagematch.domain.external.IMatchRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.MatchState;
import org.efrei.five.apimanagematch.domain.valueobjects.Period;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MatchDomainService implements IMatchDomainService {

    private final IMatchRepository matchRepository;
    private final ITeamDomainService teamService;
    private final IFieldDomainService fieldService;

    public MatchDomainService(IMatchRepository matchRepository, ITeamDomainService teamService, IFieldDomainService fieldDomainService) {
        this.matchRepository = matchRepository;
        this.teamService = teamService;
        this.fieldService = fieldDomainService;
    }

    @Override
    public Optional<Match> getMatch(Id id) {
        return matchRepository.findById(id);
    }

    @Override
    public void cancelMatch(Id id) {
        matchRepository.updateMatchStatus(id, MatchState.CANCELLED);
    }

    @Override
    public Match createMatch(Id teamAUuid, Id teamBUuid, Id fieldUuid, Period period) {

        Team teamA = teamService.getTeamById(teamAUuid).orElseThrow(
                () -> new NotFoundException("")
        );

        Team teamB = teamService.getTeamById(teamBUuid).orElseThrow(
                () -> new NotFoundException("")
        );

        Field field = fieldService.getFieldById(fieldUuid).orElseThrow(
                () -> new NotFoundException("")
        );

        if (!fieldService.createReservation(field.id(), period)) {
            throw new ImpossibleReservationException("");
        }

        Match match = new Match(
                new Id(UUID.randomUUID()),
                teamA,
                teamB,
                field,
                period,
                MatchState.NOT_STARTED
        );

        matchRepository.save(match);

        return match;
    }


}
