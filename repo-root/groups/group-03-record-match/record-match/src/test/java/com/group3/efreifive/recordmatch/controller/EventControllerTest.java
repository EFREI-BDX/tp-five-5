package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.EventDto;
import com.group3.efreifive.recordmatch.service.IEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private IEventService service;

    @InjectMocks
    private EventController controller;

    @Test
    void findAllDelegatesToService() {
        EventDto dto = new EventDto(UUID.randomUUID(), "GOAL", 1);
        when(service.findAll()).thenReturn(List.of(dto));

        List<EventDto> result = controller.findAll();

        assertThat(result).containsExactly(dto);
        verify(service).findAll();
    }

    @Test
    void createDelegatesToService() {
        EventDto input = new EventDto(UUID.randomUUID(), "START", 0);
        when(service.create(input)).thenReturn(input);

        EventDto result = controller.create(input);

        assertThat(result).isEqualTo(input);
        verify(service).create(input);
    }
}
