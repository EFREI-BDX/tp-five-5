package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.EventDto;
import com.group3.efreifive.recordmatch.entity.EventEntity;
import com.group3.efreifive.recordmatch.mapper.EventMapper;
import com.group3.efreifive.recordmatch.repository.EventRepository;
import com.group3.efreifive.recordmatch.service.IEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService implements IEventService {

    private final EventRepository repository;
    private final EventMapper mapper;

    public EventService(EventRepository repository, EventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventDto> findAll() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto findById(UUID id) {
        final EventEntity e = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        return mapper.entityToDto(e);
    }

    @Override
    public EventDto create(EventDto dto) {
        final EventEntity e = mapper.dtoToEntity(dto);
        repository.save(e);
        return mapper.entityToDto(e);
    }
}
