package com.group3.efreifive.recordmatch.inbound;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.service.IMatchSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

    @PostMapping("/{matchId}/end")
    public ResponseEntity<Void> matchEnded(@PathVariable UUID matchId) {
        this.matchSyncService.handleMatchEnded(matchId);
        return ResponseEntity.ok().build();
    }
}
