package com.group3.efreifive.recordmatch.service;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IMatchService {

    @Transactional(readOnly = true)
    List<MatchDto> findAll();

    @Transactional(readOnly = true)
    MatchDto findById(UUID id);

    @Transactional
    MatchDto create(MatchDto dto);
}
