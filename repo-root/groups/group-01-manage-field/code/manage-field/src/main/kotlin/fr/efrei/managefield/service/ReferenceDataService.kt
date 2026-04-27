package fr.efrei.managefield.service

import fr.efrei.managefield.service.dto.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.ReservationStatusViewResultDto

/**
 * Defines reference data read operations exposed by the application layer.
 */
interface ReferenceDataService {
    /**
     * Returns all field statuses.
     */
    fun listFieldStatuses(): List<FieldStatusViewResultDto>

    /**
     * Returns all reservation statuses.
     */
    fun listReservationStatuses(): List<ReservationStatusViewResultDto>
}
