package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.service.IMatchService;
import com.group3.efreifive.recordmatch.service.IMatchSyncService;
import com.group3.efreifive.recordmatch.service.MatchEventOutboundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service de synchronisation des matchs avec des systèmes externes
 * Ce service est responsable de la création des matchs dans le système local et 
 * de la notification des événements de match à des systèmes externes via le MatchEventOutboundService
 */
@Slf4j
@Service
class MatchSyncService implements IMatchSyncService {

    private final IMatchService matchService;
    private final MatchEventOutboundService outboundService;

    MatchSyncService(final IMatchService matchService, final MatchEventOutboundService outboundService) {
        this.matchService = matchService;
        this.outboundService = outboundService;
    }

    @Override
    public void handleMatchStarted(final MatchDto matchDto) {
        this.matchService.create(matchDto);
        this.outboundService.notifyMatchStarted(matchDto);
        MatchSyncService.log.info("Match {} started and recorded", matchDto.matchId());
    }

    @Override
    public void handleMatchEnded(final UUID matchId, final LocalDateTime endedAt) {
        final MatchDto matchDto = this.matchService.findById(matchId);
        this.outboundService.notifyMatchFinished(matchDto, endedAt);
        MatchSyncService.log.info("Match {} ended at {}", matchId, endedAt);
    }
}
