package com.jad.efreifive.managefield.mapper;

import com.jad.efreifive.managefield.dto.FieldDto;
import com.jad.efreifive.managefield.entity.FieldEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperCentralConfig.class, uses = ValueObjectMapper.class)
public interface FieldMapper {

    @Mapping(target = "statusId", source = "statusId")
    FieldDto toDto(FieldEntity entity);
}
