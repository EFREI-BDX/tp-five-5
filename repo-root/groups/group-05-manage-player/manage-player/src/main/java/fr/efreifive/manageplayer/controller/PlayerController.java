package fr.efreifive.manageplayer.controller;

import fr.efreifive.manageplayer.dto.PlayerDto;
import fr.efreifive.manageplayer.dto.PlayerRequest;
import fr.efreifive.manageplayer.service.PlayerService;
import java.util.List;
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
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerDto>> findAll() {
        return ResponseEntity.ok(this.playerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(this.playerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PlayerDto> create(@RequestBody PlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.playerService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> update(@PathVariable("id") UUID id, @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(this.playerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        this.playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
