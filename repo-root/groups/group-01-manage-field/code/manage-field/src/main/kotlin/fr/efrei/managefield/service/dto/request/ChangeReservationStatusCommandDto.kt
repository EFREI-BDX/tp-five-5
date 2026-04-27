package fr.efrei.managefield.service.dto.request

import jakarta.validation.constraints.NotBlank

/**
 * Command used by the service layer to change a reservation status.
 */
data class ChangeReservationStatusCommandDto(
    @field:NotBlank
    val fieldId: String,

    @field:NotBlank
    val reservationId: String,

    @field:NotBlank
    val statusId: String
)
