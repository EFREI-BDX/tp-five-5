package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.EventDto;
import com.group3.efreifive.recordmatch.entity.EventEntity;
import com.group3.efreifive.recordmatch.mapper.EventMapper;
import com.group3.efreifive.recordmatch.repository.EventRepository;
import com.group3.efreifive.recordmatch.service.DomainErrorCode;
import com.group3.efreifive.recordmatch.service.RecordMatchServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository repository;

    @Mock
    private EventMapper mapper;

    @InjectMocks
    private EventService service;

    @Test
    void findAllReturnsMappedEvents() {
        EventEntity first = new EventEntity(UUID.randomUUID(), "GOAL", 1);
        EventEntity second = new EventEntity(UUID.randomUUID(), "START", 0);
        EventDto firstDto = new EventDto(first.getEventId(), "GOAL", 1);
        EventDto secondDto = new EventDto(second.getEventId(), "START", 0);
        when(repository.findAll()).thenReturn(List.of(first, second));
        when(mapper.entityToDto(first)).thenReturn(firstDto);
        when(mapper.entityToDto(second)).thenReturn(secondDto);

        List<EventDto> result = service.findAll();

        assertThat(result).containsExactly(firstDto, secondDto);
        verify(repository).findAll();
    }

    @Test
    void findByIdThrowsNotFoundWhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(RecordMatchServiceException.class)
                .extracting("domainErrorCode")
                .isEqualTo(DomainErrorCode.EVENT_NOT_FOUND);
    }
}
