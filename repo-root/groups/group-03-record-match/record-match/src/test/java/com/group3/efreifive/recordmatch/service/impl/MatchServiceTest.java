package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.entity.MatchEntity;
import com.group3.efreifive.recordmatch.mapper.MatchMapper;
import com.group3.efreifive.recordmatch.repository.MatchRepository;
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
class MatchServiceTest {

    @Mock
    private MatchRepository repository;

    @Mock
    private MatchMapper mapper;

    @InjectMocks
    private MatchService service;

    @Test
    void findAllReturnsMappedMatches() {
        LocalDateTime startedAt = LocalDateTime.now();
        MatchEntity entity = new MatchEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), startedAt, 40);
        MatchDto dto = new MatchDto(entity.getMatchId(), entity.getTeam1Id(), entity.getTeam2Id(), startedAt, 40);
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(dto);

        List<MatchDto> result = service.findAll();

        assertThat(result).containsExactly(dto);
        verify(repository).findAll();
    }

    @Test
    void findByIdThrowsNotFoundWhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(RecordMatchServiceException.class)
                .extracting("domainErrorCode")
                .isEqualTo(DomainErrorCode.MATCH_NOT_FOUND);
    }
}
