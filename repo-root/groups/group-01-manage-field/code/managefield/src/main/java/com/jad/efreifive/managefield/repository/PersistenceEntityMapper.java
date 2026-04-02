package com.jad.efreifive.managefield.repository;

import com.jad.efreifive.managefield.entity.FieldEntity;
import com.jad.efreifive.managefield.entity.FieldStatusEntity;
import com.jad.efreifive.managefield.entity.ReservationEntity;
import com.jad.efreifive.managefield.entity.ReservationStatusEntity;
import com.jad.efreifive.managefield.persistence.ActiveFieldViewRecord;
import com.jad.efreifive.managefield.persistence.BlockingReservationViewRecord;
import com.jad.efreifive.managefield.persistence.FieldStatusViewRecord;
import com.jad.efreifive.managefield.persistence.FieldViewRecord;
import com.jad.efreifive.managefield.persistence.ReservationStatusViewRecord;
import com.jad.efreifive.managefield.vo.FieldName;
import com.jad.efreifive.managefield.vo.FieldStatusCode;
import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import com.jad.efreifive.managefield.vo.TimeSlot;

final class PersistenceEntityMapper {

    private PersistenceEntityMapper() {
    }

    static FieldEntity toDomain(FieldViewRecord record) {
        return FieldEntity.builder()
            .id(Id.of(record.getId()))
            .name(FieldName.of(record.getName()))
            .statusId(Id.of(record.getStatusId()))
            .build();
    }

    static FieldEntity toDomain(ActiveFieldViewRecord record) {
        return FieldEntity.builder()
            .id(Id.of(record.getId()))
            .name(FieldName.of(record.getName()))
            .statusId(Id.of(record.getStatusId()))
            .build();
    }

    static FieldStatusEntity toDomain(FieldStatusViewRecord record) {
        return FieldStatusEntity.builder()
            .id(Id.of(record.getId()))
            .code(FieldStatusCode.of(record.getCode()))
            .label(record.getLabel())
            .build();
    }

    static ReservationStatusEntity toDomain(ReservationStatusViewRecord record) {
        return ReservationStatusEntity.builder()
            .id(Id.of(record.getId()))
            .code(ReservationStatusCode.of(record.getCode()))
            .label(record.getLabel())
            .build();
    }

    static ReservationEntity toDomain(BlockingReservationViewRecord record) {
        return ReservationEntity.builder()
            .id(Id.of(record.getId()))
            .fieldId(Id.of(record.getFieldId()))
            .statusId(Id.of(record.getStatusId()))
            .timeSlot(TimeSlot.of(record.getDate(), record.getStartTime(), record.getEndTime()))
            .build();
    }
}
