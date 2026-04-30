package fr.efreifive.manageplayer.service;

import fr.efreifive.manageplayer.repository.PlayerRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PlayerSyncService implements IPlayerSyncService {
    private final PlayerRepository playerRepository;

    public PlayerSyncService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void playerJoinedTeam(UUID playerId, UUID teamId) {
        playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        playerRepository.addTeam(playerId, teamId);
    }

    @Override
    public void playerLeftTeam(UUID playerId, UUID teamId) {
        playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        playerRepository.removeTeam(playerId, teamId);
    }
}
