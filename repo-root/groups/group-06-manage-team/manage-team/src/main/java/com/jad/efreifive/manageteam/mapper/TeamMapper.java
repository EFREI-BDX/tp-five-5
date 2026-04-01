package com.jad.efreifive.manageteam.mapper;

import com.jad.efreifive.manageteam.dto.TeamDto;
import com.jad.efreifive.manageteam.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    
    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(entity.getId()))")
    @Mapping(target = "idTeamLeader", expression = "java(entity.getIdTeamLeader() != null ? java.util.UUID.fromString(entity.getIdTeamLeader()) : null)")
    TeamDto entityToDto(TeamEntity entity);
    
    @Mapping(target = "id", expression = "java(dto.id() != null ? dto.id().toString() : null)")
    @Mapping(target = "idTeamLeader", expression = "java(dto.idTeamLeader() != null ? dto.idTeamLeader().toString() : null)")
    TeamEntity dtoToEntity(TeamDto dto);
}

