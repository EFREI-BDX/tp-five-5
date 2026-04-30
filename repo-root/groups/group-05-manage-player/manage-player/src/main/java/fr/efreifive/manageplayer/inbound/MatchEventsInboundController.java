package fr.efreifive.manageplayer.inbound;

import fr.efreifive.manageplayer.event.in.MatchPlayerEvent;
import fr.efreifive.manageplayer.service.IPlayerSyncService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events/matches")
public class MatchEventsInboundController {
    private final IPlayerSyncService playerSyncService;

    public MatchEventsInboundController(IPlayerSyncService playerSyncService) {
        this.playerSyncService = playerSyncService;
    }

    @PostMapping("/player")
    public ResponseEntity<Void> matchPlayerEvent(@Valid @RequestBody MatchPlayerEvent event) {
        playerSyncService.matchPlayerEvent(event);
        return ResponseEntity.accepted().build();
    }
}
