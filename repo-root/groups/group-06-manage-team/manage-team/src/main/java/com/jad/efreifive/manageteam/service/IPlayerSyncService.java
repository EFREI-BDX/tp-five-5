package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.dto.PlayerDto;

import java.util.UUID;

public interface IPlayerSyncService {
    void handlePlayerCreated(PlayerDto playerDto);

    void handlePlayerUpdated(PlayerDto playerDto);

    void handlePlayerDeleted(UUID id);
}
