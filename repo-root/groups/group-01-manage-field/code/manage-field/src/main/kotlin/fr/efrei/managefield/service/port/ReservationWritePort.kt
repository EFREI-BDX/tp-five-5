package fr.efrei.managefield.service.port

import fr.efrei.managefield.domain.enums.ReservationStatusCode
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.TimeSlot

/**
 * Application port for reservation write operations.
 */
interface ReservationWritePort {
    /**
     * Creates a reservation and returns its identifier.
     */
    fun createReservation(
        reservationId: DomainId,
        fieldId: DomainId,
        status: ReservationStatusCode,
        slot: TimeSlot
    ): DomainId

    /**
     * Changes the status of an existing reservation.
     */
    fun changeReservationStatus(fieldId: DomainId, reservationId: DomainId, status: ReservationStatusCode)
}
