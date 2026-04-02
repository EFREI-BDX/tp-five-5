package com.jad.efreifive.managefield.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vw_reservation_statuses")
@Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationStatusViewRecord {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
