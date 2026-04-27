package com.jad.efreifive.manageteam.inbound;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.service.IPlayerSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inbounds/players")
public class PlayerEventsInboundController {
    private final IPlayerSyncService playerSyncService;

    public PlayerEventsInboundController(IPlayerSyncService playerSyncService) {
        this.playerSyncService = playerSyncService;
    }

    @PostMapping
    public ResponseEntity<Void> playerCreated(@RequestBody PlayerDto playerDto) {
        this.playerSyncService.handlePlayerCreated(playerDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> playerUpdated(@RequestBody PlayerDto playerDto) {
        this.playerSyncService.handlePlayerUpdated(playerDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> playerDeleted(@PathVariable UUID id) {
        this.playerSyncService.handlePlayerDeleted(id);
        return ResponseEntity.ok().build();
    }
}
