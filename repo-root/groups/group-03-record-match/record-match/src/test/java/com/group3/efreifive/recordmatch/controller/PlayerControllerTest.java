package com.group3.efreifive.recordmatch.controller;

import com.group3.efreifive.recordmatch.dto.PlayerDto;
import com.group3.efreifive.recordmatch.service.IPlayerService;
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
class PlayerControllerTest {

    @Mock
    private IPlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @Test
    void findByTeamReturnsEmptyListWhenTeamIdMissing() {
        List<PlayerDto> result = playerController.findByTeam(null);
        assertThat(result).isEmpty();
    }

    @Test
    void findByTeamReturnsPlayersWhenTeamIdProvided() {
        UUID teamId = UUID.randomUUID();
        PlayerDto player = new PlayerDto(UUID.randomUUID(), teamId);
        when(playerService.findByTeamId(teamId)).thenReturn(List.of(player));

        List<PlayerDto> result = playerController.findByTeam(teamId);

        assertThat(result).containsExactly(player);
        verify(playerService).findByTeamId(teamId);
    }

    @Test
    void findByIdDelegatesToService() {
        UUID playerId = UUID.randomUUID();
        PlayerDto expected = new PlayerDto(playerId, UUID.randomUUID());
        when(playerService.findById(playerId)).thenReturn(expected);

        PlayerDto result = playerController.findById(playerId);

        assertThat(result).isEqualTo(expected);
        verify(playerService).findById(playerId);
    }

    @Test
    void createDelegatesToService() {
        PlayerDto request = new PlayerDto(UUID.randomUUID(), UUID.randomUUID());
        when(playerService.create(request)).thenReturn(request);

        PlayerDto result = playerController.create(request);

        assertThat(result).isEqualTo(request);
        verify(playerService).create(request);
    }
}
