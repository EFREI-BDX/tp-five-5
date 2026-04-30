package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.PlayerDto;
import com.group3.efreifive.recordmatch.entity.PlayerEntity;
import com.group3.efreifive.recordmatch.mapper.PlayerMapper;
import com.group3.efreifive.recordmatch.repository.PlayerRepository;
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
class PlayerServiceTest {

    @Mock
    private PlayerRepository repository;

    @Mock
    private PlayerMapper mapper;

    @InjectMocks
    private PlayerService service;

    @Test
    void findByIdReturnsMappedPlayerWhenExisting() {
        UUID playerId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        PlayerEntity entity = new PlayerEntity(playerId, teamId);
        PlayerDto expected = new PlayerDto(playerId, teamId);

        when(repository.findById(playerId)).thenReturn(Optional.of(entity));
        when(mapper.entityToDto(entity)).thenReturn(expected);

        PlayerDto result = service.findById(playerId);

        assertThat(result).isEqualTo(expected);
        verify(repository).findById(playerId);
        verify(mapper).entityToDto(entity);
    }

    @Test
    void findByIdThrowsNotFoundWhenMissing() {
        UUID missingId = UUID.randomUUID();
        when(repository.findById(missingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(missingId))
                .isInstanceOf(RecordMatchServiceException.class)
                .extracting("domainErrorCode")
                .isEqualTo(DomainErrorCode.PLAYER_NOT_FOUND);
    }

    @Test
    void findByTeamIdReturnsMappedPlayers() {
        UUID teamId = UUID.randomUUID();
        PlayerEntity first = new PlayerEntity(UUID.randomUUID(), teamId);
        PlayerEntity second = new PlayerEntity(UUID.randomUUID(), teamId);
        PlayerDto firstDto = new PlayerDto(first.getPlayerId(), teamId);
        PlayerDto secondDto = new PlayerDto(second.getPlayerId(), teamId);

        when(repository.findByTeamId(teamId)).thenReturn(List.of(first, second));
        when(mapper.entityToDto(first)).thenReturn(firstDto);
        when(mapper.entityToDto(second)).thenReturn(secondDto);

        List<PlayerDto> result = service.findByTeamId(teamId);

        assertThat(result).containsExactly(firstDto, secondDto);
        verify(repository).findByTeamId(teamId);
    }

    @Test
    void createSavesMappedEntityAndReturnsMappedDto() {
        UUID playerId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        PlayerDto input = new PlayerDto(playerId, teamId);
        PlayerEntity entity = new PlayerEntity(playerId, teamId);
        PlayerDto output = new PlayerDto(playerId, teamId);

        when(mapper.dtoToEntity(input)).thenReturn(entity);
        when(mapper.entityToDto(entity)).thenReturn(output);

        PlayerDto result = service.create(input);

        assertThat(result).isEqualTo(output);
        verify(mapper).dtoToEntity(input);
        verify(repository).save(entity);
        verify(mapper).entityToDto(entity);
    }
}
