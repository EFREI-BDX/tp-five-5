package com.group3.efreifive.recordmatch.service;

import com.group3.efreifive.recordmatch.dto.PlayerDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IPlayerService {

    @Transactional(readOnly = true)
    PlayerDto findById(UUID id);

    @Transactional(readOnly = true)
    List<PlayerDto> findByTeamId(UUID teamId);

    @Transactional
    PlayerDto create(PlayerDto dto);
}
