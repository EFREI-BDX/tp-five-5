package fr.efreifive.manageplayer.controller;

import fr.efreifive.manageplayer.dto.CreatePlayerRequest;
import fr.efreifive.manageplayer.dto.CreatePlayerResponse;
import fr.efreifive.manageplayer.dto.DeletePlayerResponse;
import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.UpdatePlayerRequest;
import fr.efreifive.manageplayer.dto.UpdatePlayerResponse;
import fr.efreifive.manageplayer.dto.UpdatePlayerStatisticsRequest;
import fr.efreifive.manageplayer.dto.UpdatePlayerStatisticsResponse;
import fr.efreifive.manageplayer.service.PlayerService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.playerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CreatePlayerResponse> create(@Valid @RequestBody CreatePlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.playerService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePlayerResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody UpdatePlayerRequest request) {
        return ResponseEntity.ok(this.playerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeletePlayerResponse> delete(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.playerService.delete(id));
    }

    @PostMapping("/{id}/statistics")
    public ResponseEntity<UpdatePlayerStatisticsResponse> updateStatistics(
        @PathVariable("id") UUID id,
        @Valid @RequestBody UpdatePlayerStatisticsRequest request
    ) {
        return ResponseEntity.ok(this.playerService.updateStatistics(id, request));
    }
}
