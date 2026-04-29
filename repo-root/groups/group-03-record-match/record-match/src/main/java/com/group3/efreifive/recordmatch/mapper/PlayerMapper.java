package com.group3.efreifive.recordmatch.mapper;

import com.group3.efreifive.recordmatch.dto.PlayerDto;
import com.group3.efreifive.recordmatch.entity.PlayerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerDto entityToDto(PlayerEntity entity);

    PlayerEntity dtoToEntity(PlayerDto dto);
}