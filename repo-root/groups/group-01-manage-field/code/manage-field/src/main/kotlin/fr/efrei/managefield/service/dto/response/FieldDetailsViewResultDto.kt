package fr.efrei.managefield.service.dto.response

/**
 * Detailed read model returned by the service layer for a field lookup.
 */
data class FieldDetailsViewResultDto(
    val id: String,
    val name: String,
    val statusId: String,
    val status: FieldStatusViewResultDto,
    val reservations: List<ReservationViewResultDto>
)
