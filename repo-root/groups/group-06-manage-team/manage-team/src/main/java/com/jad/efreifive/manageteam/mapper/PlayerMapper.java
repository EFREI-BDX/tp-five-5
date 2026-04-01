package com.jad.efreifive.manageteam.mapper;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    
    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(entity.getId()))")
    @Mapping(target = "idTeam", expression = "java(entity.getIdTeam() != null ? java.util.UUID.fromString(entity.getIdTeam()) : null)")
    PlayerDto entityToDto(PlayerEntity entity);
    
    @Mapping(target = "id", expression = "java(dto.id() != null ? dto.id().toString() : null)")
    @Mapping(target = "idTeam", expression = "java(dto.idTeam() != null ? dto.idTeam().toString() : null)")
    PlayerEntity dtoToEntity(PlayerDto dto);
}

