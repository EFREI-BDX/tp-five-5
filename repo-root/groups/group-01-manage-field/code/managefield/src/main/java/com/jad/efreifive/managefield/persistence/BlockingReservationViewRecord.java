package com.jad.efreifive.managefield.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vw_blocking_reservations")
@Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = BlockingReservationViewRecord.CREATE_RESERVATION_PROCEDURE,
        procedureName = "sp_create_reservation",
        resultClasses = BlockingReservationViewRecord.class,
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_field_id", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_status_id", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_date", type = LocalDate.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_start_time", type = LocalTime.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_end_time", type = LocalTime.class)
        }
    ),
    @NamedStoredProcedureQuery(
        name = BlockingReservationViewRecord.UPDATE_STATUS_PROCEDURE,
        procedureName = "sp_update_reservation_status",
        resultClasses = BlockingReservationViewRecord.class,
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_field_id", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_reservation_id", type = String.class),
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_status_id", type = String.class)
        }
    )
})
public class BlockingReservationViewRecord {

    public static final String CREATE_RESERVATION_PROCEDURE = "BlockingReservationViewRecord.createReservation";
    public static final String UPDATE_STATUS_PROCEDURE = "BlockingReservationViewRecord.updateStatus";

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "field_id", nullable = false, length = 36)
    private String fieldId;

    @Column(name = "status_id", nullable = false, length = 36)
    private String statusId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
}
