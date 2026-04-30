package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.EventDto;
import com.group3.efreifive.recordmatch.service.IEventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/event")
public class EventController {

    private final IEventService service;

    public EventController(IEventService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EventDto findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(@RequestBody EventDto dto) {
        return service.create(dto);
    }
}
