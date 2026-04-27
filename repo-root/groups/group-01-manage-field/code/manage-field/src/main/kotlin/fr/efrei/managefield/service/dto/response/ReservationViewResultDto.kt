package fr.efrei.managefield.service.dto.response

/**
 * Read model returned by the service layer for a reservation.
 */
data class ReservationViewResultDto(
    val id: String,
    val fieldId: String,
    val statusId: String,
    val status: ReservationStatusViewResultDto,
    val date: String,
    val startTime: String,
    val endTime: String
)
