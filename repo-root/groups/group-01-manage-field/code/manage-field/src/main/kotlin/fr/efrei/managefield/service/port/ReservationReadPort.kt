package fr.efrei.managefield.service.port

import fr.efrei.managefield.domain.entity.Reservation
import fr.efrei.managefield.domain.valueobject.DomainId
import java.time.LocalDate

/**
 * Application port for reservation read operations.
 */
interface ReservationReadPort {
    /**
     * Lists reservations attached to one field.
     */
    fun listByFieldId(fieldId: DomainId): List<Reservation>

    /**
     * Lists reservations that can block availability on a date.
     */
    fun listBlockingByDate(date: LocalDate): List<Reservation>

    /**
     * Finds a reservation attached to one field.
     */
    fun findByIdForField(reservationId: DomainId, fieldId: DomainId): Reservation?
}
