package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.mapper.PlayerMapper;
import com.jad.efreifive.manageteam.repository.PlayerRepository;
import com.jad.efreifive.manageteam.repository.result.PersistenceOperationResult;
import com.jad.efreifive.manageteam.valueobject.Id;
import com.jad.efreifive.manageteam.valueobject.Name;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> findAll() {
        PlayerService.log.debug("Listing all players");
        List<PlayerDto> players = this.playerRepository.findAll().stream().map(this.playerMapper::entityToDto).toList();
        PlayerService.log.debug("Loaded {} players", players.size());
        return players;
    }

    @Transactional(readOnly = true)
    public PlayerDto findById(UUID id) {
        PlayerService.log.debug("Looking up player with id={}", id);
        return this.playerRepository.findById(id.toString())
                .map(entity -> {
                    PlayerService.log.debug("Player found: {}", entity);
                    return this.playerMapper.entityToDto(entity);
                })
                .orElseThrow(() -> {
                    PlayerService.log.warn("Player not found with id={}", id);
                    return new PlayerNotFoundException("Player not found: " + id);
                });
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> findByTeamId(UUID teamId) {
        PlayerService.log.debug("Listing players by teamId={}", teamId);
        List<PlayerDto> players = this.playerRepository.findByIdTeam(teamId.toString()).stream().map(
                this.playerMapper::entityToDto).toList();
        PlayerService.log.debug("Loaded {} players for teamId={}", players.size(), teamId);
        return players;
    }

    @Transactional
    public void create(final Id id, final Name displayName) {
        PlayerService.log.info("Creating player: id={}, displayName={}", id, displayName);
        this.assertSuccess(this.playerRepository.create(id, displayName), "Player create failed");
        PlayerService.log.info("Player created successfully: id={}", id);
    }

    private void assertSuccess(PersistenceOperationResult result, String defaultMessage) {
        if (!result.success()) {
            String message = result.message() == null || result.message().isBlank() ? defaultMessage : result.message();
            PlayerService.log.error("Operation failed: {}", message);
            throw new ServiceOperationException(message);
        }
    }

    @Transactional
    public void update(final Id id, final Name displayName) {
        PlayerService.log.info("Updating player: id={}, displayName={}", id, displayName);
        this.assertSuccess(this.playerRepository.update(id, displayName), "Player update failed");
        PlayerService.log.info("Player updated successfully: id={}", id);
    }

    @Transactional
    public void delete(final Id id) {
        PlayerService.log.info("Deleting player: id={}", id);
        this.assertSuccess(this.playerRepository.delete(id), "Player delete failed");
        PlayerService.log.info("Player deleted successfully: id={}", id);
    }

    @Transactional
    public void assignTeam(final Id id, final Id teamId) {
        PlayerService.log.info("Assigning player to team: playerId={}, teamId={}", id, teamId);
        this.assertSuccess(this.playerRepository.assignTeam(id, teamId),
                           "Player assign team failed");
        PlayerService.log.info("Player assigned to team successfully: playerId={}, teamId={}", id, teamId);
    }

    @Transactional
    public void unassignTeam(final Id id) {
        PlayerService.log.info("Unassigning player from team: playerId={}", id);
        this.assertSuccess(this.playerRepository.unassignTeam(id), "Player unassign team failed");
        PlayerService.log.info("Player unassigned from team successfully: playerId={}", id);
    }
}
