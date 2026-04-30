package com.group3.efreifive.recordmatch.inbound;

import com.group3.efreifive.recordmatch.dto.EndMatchDto;
import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.service.IMatchSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlleur pour les événements entrants liés aux matchs, tels que le début et la fin d'un match.
 * Ce controller reçoit les notifications de ces événements et délègue leur traitement 
 * au service de synchronisation des matchs (IMatchSyncService).
 */
@RestController
@RequestMapping("/api/inbounds/matches")
public class MatchInboundController {

    private final IMatchSyncService matchSyncService;

    public MatchInboundController(final IMatchSyncService matchSyncService) {
        this.matchSyncService = matchSyncService;
    }

    @PostMapping
    public ResponseEntity<Void> matchStarted(@RequestBody MatchDto matchDto) {
        this.matchSyncService.handleMatchStarted(matchDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/end")
    public ResponseEntity<Void> matchEnded(@RequestBody EndMatchDto dto) {
        this.matchSyncService.handleMatchEnded(dto.matchId(), dto.endedAt());
        return ResponseEntity.ok().build();
    }
}
