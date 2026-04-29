package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.service.IMatchEventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/match-events")
public class MatchEventController {

    private final IMatchEventService service;

    public MatchEventController(IMatchEventService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public MatchEventDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/by-match/{matchId}")
    public List<MatchEventDto> findByMatch(@PathVariable UUID matchId) {
        return service.findByMatchId(matchId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchEventDto recordEvent(@RequestBody MatchEventDto dto) {
        return service.recordEvent(dto);
    }
}
