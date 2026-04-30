package fr.efrei.managefield.domain.service

import fr.efrei.managefield.domain.entity.Field
import fr.efrei.managefield.domain.entity.Reservation
import fr.efrei.managefield.domain.enums.ReservationStatusCode
import fr.efrei.managefield.domain.valueobject.TimeSlot

/**
 * Domain policy protecting reservation invariants before persistence.
 */
class ReservationPolicy {
    /**
     * Ensures a reservation can be created for the requested field and slot.
     */
    fun requireCreationAllowed(
        field: Field,
        requestedStatus: ReservationStatusCode,
        requestedSlot: TimeSlot,
        existingReservations: Collection<Reservation>
    ) {
        field.requireReservable()

        if (requestedStatus.blocksAvailability() && existingReservations.any { it.blocks(requestedSlot) }) {
            throw IllegalArgumentException("reservation overlaps an active reservation")
        }
    }
}
