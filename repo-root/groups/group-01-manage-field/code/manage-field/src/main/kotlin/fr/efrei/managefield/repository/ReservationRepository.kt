package fr.efrei.managefield.repository

import fr.efrei.managefield.entity.ReservationEntity
import fr.efrei.managefield.repository.procedural.CreateReservationProcedureResult
import fr.efrei.managefield.repository.procedural.SimpleProcedureResult
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime

/**
 * Repository exposing reservation reads through a view and writes through
 * stored procedures.
 */
@Repository
interface ReservationRepository : JpaRepository<ReservationEntity, String> {
    /**
     * Returns reservations for availability checks.
     */
    fun findAllByDateAndStatusIdIn(date: LocalDate, statusIds: Collection<String>): List<ReservationEntity>

    /**
     * Calls the `app_create_reservation` stored procedure.
     */
    @Procedure(procedureName = "app_create_reservation")
    fun createReservation(
        @Param("p_reservation_id") reservationId: String?,
        @Param("p_field_id") fieldId: String,
        @Param("p_status_id") statusId: String,
        @Param("p_date") date: LocalDate,
        @Param("p_start_time") startTime: LocalTime,
        @Param("p_end_time") endTime: LocalTime
    ): CreateReservationProcedureResult

    /**
     * Calls the `app_change_reservation_status` stored procedure.
     */
    @Procedure(procedureName = "app_change_reservation_status")
    fun changeReservationStatus(
        @Param("p_field_id") fieldId: String,
        @Param("p_reservation_id") reservationId: String,
        @Param("p_status_id") statusId: String
    ): SimpleProcedureResult
}
