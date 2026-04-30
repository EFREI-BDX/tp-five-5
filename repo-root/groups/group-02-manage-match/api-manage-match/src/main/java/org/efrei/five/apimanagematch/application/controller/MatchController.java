package org.efrei.five.apimanagematch.application.controller;

import org.efrei.five.apimanagematch.application.service.MatchApplicationService;
import org.efrei.five.apimanagematch.domain.command.common.MatchResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchApplicationService service;

    public MatchController(MatchApplicationService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDto> get(@PathVariable UUID id) {
        return service.getMatch(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable UUID id) {
        service.cancelMatch(id);
    }

    @PostMapping
    public ResponseEntity<MatchResponseDto> create(@RequestBody MatchRequest request) {
        return ResponseEntity.ok(service.create(request));
    }
}