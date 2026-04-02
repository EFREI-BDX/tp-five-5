package com.jad.efreifive.managefield.entity;

import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.TimeSlot;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "reservation",
    indexes = {
        @Index(name = "idx_reservation_field_slot", columnList = "field_id,date,start_time,end_time"),
        @Index(name = "idx_reservation_status", columnList = "status_id")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    @jakarta.persistence.Id
    @Convert(converter = com.jad.efreifive.managefield.converter.IdAttributeConverter.class)
    @Column(name = "id", nullable = false, length = 36)
    private Id id;

    @Convert(converter = com.jad.efreifive.managefield.converter.IdAttributeConverter.class)
    @Column(name = "field_id", nullable = false, length = 36)
    private Id fieldId;

    @Convert(converter = com.jad.efreifive.managefield.converter.IdAttributeConverter.class)
    @Column(name = "status_id", nullable = false, length = 36)
    private Id statusId;

    @Embedded
    private TimeSlot timeSlot;
}
