package com.group3.efreifive.recordmatch.mapper;

import com.group3.efreifive.recordmatch.dto.EventDto;
import com.group3.efreifive.recordmatch.entity.EventEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDto entityToDto(EventEntity entity);

    EventEntity dtoToEntity(EventDto dto);
}