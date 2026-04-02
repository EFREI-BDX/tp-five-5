package com.jad.efreifive.managefield.mapper;

import com.jad.efreifive.managefield.vo.FieldName;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapperCentralConfig.class)
public interface ValueObjectMapper {

    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    default String map(Id value) {
        return value == null ? null : value.asString();
    }

    default String map(FieldName value) {
        return value == null ? null : value.value();
    }

    default String map(FieldStatusCode value) {
        return value == null ? null : value.value();
    }

    default String map(ReservationStatusCode value) {
        return value == null ? null : value.value();
    }

    default String map(LocalDate value) {
        return value == null ? null : value.toString();
    }

    default String map(LocalTime value) {
        return value == null ? null : value.format(TIME_FORMATTER);
    }
}
