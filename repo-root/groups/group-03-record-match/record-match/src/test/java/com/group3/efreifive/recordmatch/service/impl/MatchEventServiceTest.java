package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.entity.MatchEventEntity;
import com.group3.efreifive.recordmatch.mapper.MatchEventMapper;
import com.group3.efreifive.recordmatch.repository.MatchEventRepository;
import com.group3.efreifive.recordmatch.service.DomainErrorCode;
import com.group3.efreifive.recordmatch.service.RecordMatchServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatchEventServiceTest {

    @Mock
    private MatchEventRepository repository;

    @Mock
    private MatchEventMapper mapper;

    @InjectMocks
    private MatchEventService service;

    @Test
    void findByMatchIdReturnsMappedEvents() {
        UUID matchId = UUID.randomUUID();
        LocalDateTime occuredAt = LocalDateTime.now();
        MatchEventEntity entity = new MatchEventEntity(
                UUID.randomUUID(), matchId, UUID.randomUUID(), UUID.randomUUID(), null, occuredAt, true, null, null
        );
        MatchEventDto dto = new MatchEventDto(
                entity.getMatchEventId(), matchId, entity.getEventId(), entity.getPlayer1Id(), entity.getPlayer2Id(),
                occuredAt, true, null, null
        );
        when(repository.findByMatchId(matchId)).thenReturn(List.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(dto);

        List<MatchEventDto> result = service.findByMatchId(matchId);

        assertThat(result).containsExactly(dto);
        verify(repository).findByMatchId(matchId);
    }

    @Test
    void findByIdThrowsNotFoundWhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(RecordMatchServiceException.class)
                .extracting("domainErrorCode")
                .isEqualTo(DomainErrorCode.MATCH_EVENT_NOT_FOUND);
    }
}
