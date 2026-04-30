package fr.efrei.managefield.domain.entity

import fr.efrei.managefield.domain.enums.ReservationStatusCode
import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.TimeSlot
import java.util.Objects

/**
 * Domain aggregate representing field occupancy for one time slot.
 */
@ConsistentCopyVisibility
data class Reservation private constructor(
    val id: DomainId,
    val fieldId: DomainId,
    val status: ReservationStatusCode,
    val slot: TimeSlot
) {
    companion object {
        /**
         * Restores a reservation aggregate from trusted persistence data.
         */
        fun restore(
            id: DomainId,
            fieldId: DomainId,
            status: ReservationStatusCode,
            slot: TimeSlot
        ): Reservation {
            return Reservation(
                id = Objects.requireNonNull(id, "reservation id must not be null"),
                fieldId = Objects.requireNonNull(fieldId, "field id must not be null"),
                status = Objects.requireNonNull(status, "reservation status must not be null"),
                slot = Objects.requireNonNull(slot, "reservation slot must not be null")
            )
        }
    }

    /**
     * Returns true when this reservation blocks the provided slot.
     */
    fun blocks(requestedSlot: TimeSlot): Boolean {
        return status.blocksAvailability() && slot.overlaps(requestedSlot.startTime, requestedSlot.endTime)
    }

    /**
     * Returns a new reservation instance with the requested status.
     */
    fun changeStatus(newStatus: ReservationStatusCode): Reservation {
        return copy(status = Objects.requireNonNull(newStatus, "reservation status must not be null"))
    }
}
