package com.jad.efreifive.manageteam.service;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.mapper.PlayerMapper;
import com.jad.efreifive.manageteam.repository.PlayerRepository;
import com.jad.efreifive.manageteam.repository.result.OperationResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        return this.playerRepository.findAll().stream().map(this.playerMapper::entityToDto).toList();
    }

    @Transactional(readOnly = true)
    public PlayerDto findById(UUID id) {
        return this.playerRepository.findById(id.toString())
                .map(this.playerMapper::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<PlayerDto> findByTeamId(UUID teamId) {
        return this.playerRepository.findByIdTeam(teamId.toString()).stream().map(this.playerMapper::entityToDto).toList();
    }

    @Transactional
    public void create(UUID id, String displayName) {
        this.assertSuccess(this.playerRepository.create(id.toString(), displayName), "Player create failed");
    }

    @Transactional
    public void update(UUID id, String displayName) {
        this.assertSuccess(this.playerRepository.update(id.toString(), displayName), "Player update failed");
    }

    @Transactional
    public void delete(UUID id) {
        this.assertSuccess(this.playerRepository.delete(id.toString()), "Player delete failed");
    }

    @Transactional
    public void assignTeam(UUID id, UUID teamId) {
        this.assertSuccess(this.playerRepository.assignTeam(id.toString(), teamId.toString()), "Player assign team failed");
    }

    @Transactional
    public void unassignTeam(UUID id) {
        this.assertSuccess(this.playerRepository.unassignTeam(id.toString()), "Player unassign team failed");
    }

    private void assertSuccess(OperationResult result, String defaultMessage) {
        if (!result.success()) {
            throw new ServiceOperationException(result.message() == null || result.message().isBlank() ? defaultMessage : result.message());
        }
    }
}

