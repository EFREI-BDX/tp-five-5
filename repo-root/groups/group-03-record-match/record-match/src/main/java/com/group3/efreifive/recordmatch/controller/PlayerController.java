package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.PlayerDto;
import com.group3.efreifive.recordmatch.service.IPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final IPlayerService service;

    public PlayerController(IPlayerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public PlayerDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public List<PlayerDto> findByTeam(@RequestParam(required = false) UUID teamId) {
        if (teamId == null) {
            return List.of();
        }
        return service.findByTeamId(teamId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDto create(@RequestBody PlayerDto dto) {
        return service.create(dto);
    }
}
