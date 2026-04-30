package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.service.IMatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    @Mock
    private IMatchService service;

    @InjectMocks
    private MatchController controller;

    @Test
    void findByIdDelegatesToService() {
        UUID id = UUID.randomUUID();
        MatchDto dto = new MatchDto(id, UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), 40);
        when(service.findById(id)).thenReturn(dto);

        MatchDto result = controller.findById(id);

        assertThat(result).isEqualTo(dto);
        verify(service).findById(id);
    }

    @Test
    void findAllDelegatesToService() {
        MatchDto dto = new MatchDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), 40);
        when(service.findAll()).thenReturn(List.of(dto));

        List<MatchDto> result = controller.findAll();

        assertThat(result).containsExactly(dto);
        verify(service).findAll();
    }
}
