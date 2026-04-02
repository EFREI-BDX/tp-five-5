package com.jad.efreifive.manageteam.mapper;

import com.jad.efreifive.manageteam.dto.PlayerDto;
import com.jad.efreifive.manageteam.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(entity.getId()))")
    @Mapping(target = "idTeam", expression = "java(entity.getIdTeam() != null ? java.util.UUID.fromString(entity.getIdTeam()) : null)")
    PlayerDto entityToDto(PlayerEntity entity);
}

