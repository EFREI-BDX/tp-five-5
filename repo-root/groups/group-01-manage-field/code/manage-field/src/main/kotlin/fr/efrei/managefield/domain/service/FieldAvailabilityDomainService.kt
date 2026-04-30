package fr.efrei.managefield.domain.service

import fr.efrei.managefield.domain.entity.Field
import fr.efrei.managefield.domain.entity.Reservation
import fr.efrei.managefield.domain.valueobject.TimeSlot

/**
 * Pure domain service computing availability from fields, reservations, and a requested slot.
 */
class FieldAvailabilityDomainService {
    /**
     * Returns reservable fields not blocked by a reservation overlapping the requested slot.
     */
    fun listAvailableFields(
        fields: Collection<Field>,
        blockingReservations: Collection<Reservation>,
        requestedSlot: TimeSlot
    ): List<Field> {
        val blockedFieldIds = blockingReservations
            .filter { it.blocks(requestedSlot) }
            .map { it.fieldId.value }
            .toSet()

        return fields
            .filter { it.isReservable() }
            .filterNot { it.id.value in blockedFieldIds }
    }
}
