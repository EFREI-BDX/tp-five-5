package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Name;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
class PlayerSyncService implements IPlayerSyncService {
    private final PlayerService playerService;

    PlayerSyncService(final PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void handlePlayerCreated(PlayerDto playerDto) {
        this.playerService.create(new Id(playerDto.id()), new Name(playerDto.firstName(), playerDto.lastName()));
    }

    @Override
    public void handlePlayerUpdated(PlayerDto playerDto) {
        this.playerService.update(new Id(playerDto.id()), new Name(playerDto.firstName(), playerDto.lastName()));
    }

    @Override
    public void handlePlayerDeleted(UUID id) {
        this.playerService.delete(new Id(id));
    }
}
