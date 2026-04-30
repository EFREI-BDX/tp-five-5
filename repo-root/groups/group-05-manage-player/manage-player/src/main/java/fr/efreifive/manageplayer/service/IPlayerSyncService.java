package fr.efreifive.manageplayer.service;

import fr.efreifive.manageplayer.event.in.MatchPlayerEvent;
import java.util.UUID;

public interface IPlayerSyncService {
    void playerJoinedTeam(UUID playerId, UUID teamId);

    void playerLeftTeam(UUID playerId, UUID teamId);

    void teamNameUpdated(UUID teamId, String name);

    void teamDeleted(UUID teamId);

    void matchPlayerEvent(MatchPlayerEvent event);
}
