package fr.efreifive.manageplayer.service;

import java.util.UUID;

public interface IPlayerSyncService {
    void playerJoinedTeam(UUID playerId, UUID teamId);

    void playerLeftTeam(UUID playerId, UUID teamId);
}
