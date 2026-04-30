package fr.efrei.managefield.service.port

import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto

/**
 * Application port for reference data read operations.
 */
interface ReferenceDataReadPort {
    /**
     * Lists field statuses.
     */
    fun listFieldStatuses(): List<FieldStatusViewResultDto>

    /**
     * Lists reservation statuses.
     */
    fun listReservationStatuses(): List<ReservationStatusViewResultDto>
}
