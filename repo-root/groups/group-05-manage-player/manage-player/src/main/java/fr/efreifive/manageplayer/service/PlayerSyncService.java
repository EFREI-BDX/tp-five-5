package fr.efreifive.manageplayer.service;

import fr.efreifive.manageplayer.dto.PlayerStatisticsDto;
import fr.efreifive.manageplayer.event.in.MatchPlayerEvent;
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

    @Override
    public void teamNameUpdated(UUID teamId, String name) {
        playerRepository.upsertTeam(teamId, name.trim());
    }

    @Override
    public void teamDeleted(UUID teamId) {
        playerRepository.deleteTeam(teamId);
    }

    @Override
    public void matchPlayerEvent(MatchPlayerEvent event) {
        playerRepository.findById(event.playerId()).orElseThrow(() -> new PlayerNotFoundException(event.playerId()));
        playerRepository.updateStatistics(
            event.playerId(),
            new PlayerStatisticsDto(
                event.matchesPlayed(),
                event.goalsScored(),
                event.assists(),
                event.wins(),
                event.losses(),
                event.draws(),
                event.mvps()
            )
        );
    }
}
