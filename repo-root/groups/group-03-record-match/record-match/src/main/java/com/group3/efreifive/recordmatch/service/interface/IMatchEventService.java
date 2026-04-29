package com.group3.efreifive.recordmatch.service;

import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IMatchEventService {

    @Transactional(readOnly = true)
    MatchEventDto findById(UUID id);

    @Transactional(readOnly = true)
    List<MatchEventDto> findByMatchId(UUID matchId);

    @Transactional
    MatchEventDto recordEvent(MatchEventDto dto);
}
