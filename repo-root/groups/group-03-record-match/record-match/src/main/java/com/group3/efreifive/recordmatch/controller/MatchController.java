package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.service.IMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final IMatchService service;

    public MatchController(IMatchService service) {
        this.service = service;
    }

    @GetMapping
    public List<MatchDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MatchDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchDto create(@RequestBody MatchDto dto) {
        return service.create(dto);
    }
}
