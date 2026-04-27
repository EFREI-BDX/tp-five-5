package fr.efrei.managefield.service.dto.response

/**
 * Read model returned by the service layer for a reservation status.
 */
data class ReservationStatusViewResultDto(
    val id: String,
    val code: String,
    val label: String
)
