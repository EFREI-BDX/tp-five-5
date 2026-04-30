package fr.efreifive.manageplayer.inbound;

import fr.efreifive.manageplayer.service.IPlayerSyncService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events/teams")
public class TeamEventsInboundController {
    private final IPlayerSyncService playerSyncService;

    public TeamEventsInboundController(IPlayerSyncService playerSyncService) {
        this.playerSyncService = playerSyncService;
    }

    @PostMapping("/player-joined")
    public ResponseEntity<Void> playerJoinedTeam(@RequestBody TeamPlayerEvent event) {
        playerSyncService.playerJoinedTeam(event.playerId(), event.teamId());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/player-left")
    public ResponseEntity<Void> playerLeftTeam(@RequestBody TeamPlayerEvent event) {
        playerSyncService.playerLeftTeam(event.playerId(), event.teamId());
        return ResponseEntity.accepted().build();
    }

    public record TeamPlayerEvent(UUID playerId, UUID teamId) {
    }
}
