package com.group3.efreifive.recordmatch.service.impl;

import com.group3.efreifive.recordmatch.dto.EventDto;
import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.entity.MatchEventEntity;
import com.group3.efreifive.recordmatch.mapper.MatchEventMapper;
import com.group3.efreifive.recordmatch.repository.MatchEventRepository;
import com.group3.efreifive.recordmatch.service.DomainErrorCode;
import com.group3.efreifive.recordmatch.service.IEventService;
import com.group3.efreifive.recordmatch.service.IMatchEventService;
import com.group3.efreifive.recordmatch.service.IMatchService;
import com.group3.efreifive.recordmatch.service.MatchEventOutboundService;
import com.group3.efreifive.recordmatch.service.RecordMatchServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service de gestion des événements de match
 */
@Slf4j
@Service
@Transactional
public class MatchEventService implements IMatchEventService {

    private final MatchEventRepository repository;
    private final MatchEventMapper mapper;
    private final IMatchService matchService;
    private final IEventService eventService;
    private final MatchEventOutboundService outboundService;

    public MatchEventService(final MatchEventRepository repository,
                             final MatchEventMapper mapper,
                             final IMatchService matchService,
                             final IEventService eventService,
                             final MatchEventOutboundService outboundService) {
        this.repository = repository;
        this.mapper = mapper;
        this.matchService = matchService;
        this.eventService = eventService;
        this.outboundService = outboundService;
    }

    @Override
    @Transactional(readOnly = true)
    public MatchEventDto findById(final UUID id) {
        final MatchEventEntity e = repository.findById(id).orElseThrow(() -> new RecordMatchServiceException(
                DomainErrorCode.MATCH_EVENT_NOT_FOUND,
                "MatchEvent not found: " + id));
        return mapper.entityToDto(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchEventDto> findByMatchId(final UUID matchId) {
        return repository.findByMatchId(matchId).stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MatchEventDto recordEvent(final MatchEventDto dto) {
        final MatchEventEntity e = this.mapper.dtoToEntity(dto);
        this.repository.save(e);
        final MatchEventDto saved = this.mapper.entityToDto(e);
        this.notifyOutbound(saved);
        return saved;
    }

    private void notifyOutbound(final MatchEventDto matchEventDto) {
        try {
            final MatchDto matchDto = this.matchService.findById(matchEventDto.matchId());
            final EventDto eventDto = this.eventService.findById(matchEventDto.eventId());
            final String eventType = toOutboundType(eventDto.name());
            this.outboundService.notifyMatchEvent(matchEventDto, matchDto, eventType);
        } catch (Exception ex) {
            MatchEventService.log.warn("Outbound notification failed for matchEvent {}: {}",
                    matchEventDto.matchEventId(), ex.getMessage());
        }
    }

    private static String toOutboundType(final String eventName) {
        return eventName.toUpperCase().replace(" ", "_").replace("-", "_");
    }
}
