package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.service.IMatchEventService;
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
class MatchEventControllerTest {

    @Mock
    private IMatchEventService service;

    @InjectMocks
    private MatchEventController controller;

    @Test
    void findByMatchDelegatesToService() {
        UUID matchId = UUID.randomUUID();
        MatchEventDto dto = new MatchEventDto(
                UUID.randomUUID(), matchId, UUID.randomUUID(), UUID.randomUUID(), null, LocalDateTime.now(),
                true, null, null
        );
        when(service.findByMatchId(matchId)).thenReturn(List.of(dto));

        List<MatchEventDto> result = controller.findByMatch(matchId);

        assertThat(result).containsExactly(dto);
        verify(service).findByMatchId(matchId);
    }

    @Test
    void recordEventDelegatesToService() {
        MatchEventDto input = new MatchEventDto(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, LocalDateTime.now(),
                false, null, null
        );
        when(service.recordEvent(input)).thenReturn(input);

        MatchEventDto result = controller.recordEvent(input);

        assertThat(result).isEqualTo(input);
        verify(service).recordEvent(input);
    }
}
