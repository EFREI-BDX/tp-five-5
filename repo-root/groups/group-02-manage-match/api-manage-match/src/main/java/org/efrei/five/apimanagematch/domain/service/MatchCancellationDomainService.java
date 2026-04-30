package org.efrei.five.apimanagematch.domain.service;

import org.efrei.five.apimanagematch.domain.external.IMatchByTeamRepository;
import org.efrei.five.apimanagematch.domain.external.IMatchRepository;
import org.efrei.five.apimanagematch.domain.valueobjects.Id;
import org.efrei.five.apimanagematch.domain.valueobjects.MatchState;
import org.springframework.stereotype.Service;

@Service
public class MatchCancellationDomainService implements IMatchCancellationDomainService {

    private final IMatchByTeamRepository matchByTeamRepository;
    private final IMatchRepository matchRepository;
    private final IMatchTimerService matchTimerService;

    public MatchCancellationDomainService(IMatchByTeamRepository matchByTeamRepository,
                                          IMatchRepository matchRepository,
                                          IMatchTimerService matchTimerService) {
        this.matchByTeamRepository = matchByTeamRepository;
        this.matchRepository = matchRepository;
        this.matchTimerService = matchTimerService;
    }

    @Override
    public void cancelMatchesForTeam(Id teamId) {
        matchByTeamRepository.findNotStartedMatchIdsByTeamId(teamId).forEach(matchId -> {
            matchTimerService.cancelMatchTimers(matchId);
            matchRepository.updateMatchStatus(matchId, MatchState.CANCELLED);
        });
    }
}