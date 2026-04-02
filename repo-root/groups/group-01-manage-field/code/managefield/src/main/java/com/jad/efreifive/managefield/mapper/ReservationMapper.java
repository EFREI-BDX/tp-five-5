package com.jad.efreifive.managefield.mapper;

import com.jad.efreifive.managefield.dto.ReservationDto;
import com.jad.efreifive.managefield.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperCentralConfig.class, uses = ValueObjectMapper.class)
public interface ReservationMapper {

    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "statusId", source = "statusId")
    @Mapping(target = "date", source = "timeSlot.date")
    @Mapping(target = "startTime", source = "timeSlot.startTime")
    @Mapping(target = "endTime", source = "timeSlot.endTime")
    ReservationDto toDto(ReservationEntity entity);
}
