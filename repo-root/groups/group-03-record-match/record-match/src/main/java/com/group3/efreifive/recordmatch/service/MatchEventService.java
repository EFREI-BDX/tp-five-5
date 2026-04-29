package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.entity.MatchEventEntity;
import com.group3.efreifive.recordmatch.mapper.MatchEventMapper;
import com.group3.efreifive.recordmatch.repository.MatchEventRepository;
import com.group3.efreifive.recordmatch.service.DomainErrorCode;
import com.group3.efreifive.recordmatch.service.IMatchEventService;
import com.group3.efreifive.recordmatch.service.RecordMatchServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchEventService implements IMatchEventService {

    private final MatchEventRepository repository;
    private final MatchEventMapper mapper;

    public MatchEventService(MatchEventRepository repository, MatchEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public MatchEventDto findById(UUID id) {
        final MatchEventEntity e = repository.findById(id).orElseThrow(() -> new RecordMatchServiceException(
                DomainErrorCode.MATCH_EVENT_NOT_FOUND,
                "MatchEvent not found: " + id));
        return mapper.entityToDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchEventDto> findByMatchId(UUID matchId) {
        return repository.findByMatchId(matchId).stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public MatchEventDto recordEvent(MatchEventDto dto) {
        final MatchEventEntity e = mapper.dtoToEntity(dto);
        repository.save(e);
        return mapper.entityToDto(e);
    }
}
