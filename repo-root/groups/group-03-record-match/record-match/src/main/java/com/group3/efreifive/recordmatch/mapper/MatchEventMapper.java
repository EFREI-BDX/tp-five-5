package com.group3.efreifive.recordmatch.mapper;

import com.group3.efreifive.recordmatch.dto.MatchEventDto;
import com.group3.efreifive.recordmatch.entity.MatchEventEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchEventMapper {

    MatchEventDto entityToDto(MatchEventEntity entity);

    MatchEventEntity dtoToEntity(MatchEventDto dto);
}