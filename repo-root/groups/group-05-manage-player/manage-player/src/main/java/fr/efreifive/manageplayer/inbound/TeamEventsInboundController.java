package fr.efreifive.manageplayer.inbound;

import fr.efreifive.manageplayer.event.in.PlayerJoinedTeamEvent;
import fr.efreifive.manageplayer.event.in.PlayerLeftTeamEvent;
import fr.efreifive.manageplayer.event.in.TeamDeletedEvent;
import fr.efreifive.manageplayer.event.in.TeamNameUpdatedEvent;
import fr.efreifive.manageplayer.service.IPlayerSyncService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> playerJoinedTeam(@Valid @RequestBody PlayerJoinedTeamEvent event) {
        playerSyncService.playerJoinedTeam(event.playerId(), event.teamId());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/player-left")
    public ResponseEntity<Void> playerLeftTeam(@Valid @RequestBody PlayerLeftTeamEvent event) {
        playerSyncService.playerLeftTeam(event.playerId(), event.teamId());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/name-updated")
    public ResponseEntity<Void> teamNameUpdated(@Valid @RequestBody TeamNameUpdatedEvent event) {
        playerSyncService.teamNameUpdated(event.teamId(), event.name());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/deleted")
    public ResponseEntity<Void> teamDeleted(@Valid @RequestBody TeamDeletedEvent event) {
        playerSyncService.teamDeleted(event.teamId());
        return ResponseEntity.accepted().build();
    }
}
