package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.entity.MatchEntity;
import com.group3.efreifive.recordmatch.mapper.MatchMapper;
import com.group3.efreifive.recordmatch.repository.MatchRepository;
import com.group3.efreifive.recordmatch.service.DomainErrorCode;
import com.group3.efreifive.recordmatch.service.IMatchService;
import com.group3.efreifive.recordmatch.service.RecordMatchServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MatchService implements IMatchService {

    private final MatchRepository repository;
    private final MatchMapper mapper;

    public MatchService(MatchRepository repository, MatchMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchDto> findAll() {
        return repository.findAll().stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MatchDto findById(UUID id) {
        final MatchEntity e = repository.findById(id).orElseThrow(() -> new RecordMatchServiceException(
                DomainErrorCode.MATCH_NOT_FOUND,
                "Match not found: " + id));
        return mapper.entityToDto(e);
    }

    @Override
    public MatchDto create(MatchDto dto) {
        final MatchEntity e = mapper.dtoToEntity(dto);
        repository.save(e);
        return mapper.entityToDto(e);
    }
}
