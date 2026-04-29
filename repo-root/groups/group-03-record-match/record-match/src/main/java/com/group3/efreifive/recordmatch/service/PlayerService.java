package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.PlayerDto;
import com.group3.efreifive.recordmatch.entity.PlayerEntity;
import com.group3.efreifive.recordmatch.mapper.PlayerMapper;
import com.group3.efreifive.recordmatch.repository.PlayerRepository;
import com.group3.efreifive.recordmatch.service.DomainErrorCode;
import com.group3.efreifive.recordmatch.service.IPlayerService;
import com.group3.efreifive.recordmatch.service.RecordMatchServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlayerService implements IPlayerService {

    private final PlayerRepository repository;
    private final PlayerMapper mapper;

    public PlayerService(PlayerRepository repository, PlayerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerDto findById(UUID id) {
        final PlayerEntity e = repository.findById(id).orElseThrow(() -> new RecordMatchServiceException(
                DomainErrorCode.PLAYER_NOT_FOUND,
                "Player not found: " + id));
        return mapper.entityToDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> findByTeamId(UUID teamId) {
        return repository.findByTeamId(teamId).stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public PlayerDto create(PlayerDto dto) {
        final PlayerEntity e = mapper.dtoToEntity(dto);
        repository.save(e);
        return mapper.entityToDto(e);
    }
}
