package com.jad.efreifive.managefield.entity;

import com.jad.efreifive.managefield.vo.Id;
import com.jad.efreifive.managefield.vo.ReservationStatusCode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "reservation_status",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_reservation_status_code", columnNames = "code")
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusEntity {

    @jakarta.persistence.Id
    @Convert(converter = com.jad.efreifive.managefield.converter.IdAttributeConverter.class)
    @Column(name = "id", nullable = false, length = 36)
    private Id id;

    @Column(name = "code", nullable = false, length = 32)
    private ReservationStatusCode code;

    @Column(name = "label", nullable = false, length = 128)
    private String label;
}
