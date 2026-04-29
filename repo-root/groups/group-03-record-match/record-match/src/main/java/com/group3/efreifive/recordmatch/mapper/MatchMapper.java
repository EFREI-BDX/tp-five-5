package com.group3.efreifive.recordmatch.mapper;

import com.group3.efreifive.recordmatch.dto.MatchDto;
import com.group3.efreifive.recordmatch.entity.MatchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    MatchDto entityToDto(MatchEntity entity);

    MatchEntity dtoToEntity(MatchDto dto);
}