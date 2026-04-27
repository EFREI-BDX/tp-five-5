package fr.efrei.managefield.controller.dto.response

/**
 * HTTP payload returned for a reservation status.
 */
data class ReservationStatusResponseDto(
    val id: String,
    val code: String,
    val label: String
)
