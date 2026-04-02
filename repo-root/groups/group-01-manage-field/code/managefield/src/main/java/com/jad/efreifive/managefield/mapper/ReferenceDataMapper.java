package com.jad.efreifive.managefield.mapper;

import com.jad.efreifive.managefield.dto.FieldStatusDto;
import com.jad.efreifive.managefield.dto.ReservationStatusDto;
import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import org.mapstruct.Mapper;

@Mapper(config = MapperCentralConfig.class, uses = ValueObjectMapper.class)
public interface ReferenceDataMapper {

    FieldStatusDto toDto(FieldStatusEntity entity);

    ReservationStatusDto toDto(ReservationStatusEntity entity);
}
