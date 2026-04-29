package com.group3.efreifive.recordmatch.service;

import com.group3.efreifive.recordmatch.dto.EventDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IEventService {

    @Transactional(readOnly = true)
    List<EventDto> findAll();

    @Transactional(readOnly = true)
    EventDto findById(UUID id);

    @Transactional
    EventDto create(EventDto dto);
}
