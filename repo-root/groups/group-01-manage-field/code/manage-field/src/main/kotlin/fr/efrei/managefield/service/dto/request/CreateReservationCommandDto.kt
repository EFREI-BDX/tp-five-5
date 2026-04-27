package fr.efrei.managefield.service.dto.request

import jakarta.validation.constraints.NotBlank

/**
 * Command used by the service layer to create a reservation.
 */
data class CreateReservationCommandDto(
    @field:NotBlank
    val fieldId: String,

    @field:NotBlank
    val statusId: String,

    @field:NotBlank
    val date: String,

    @field:NotBlank
    val startTime: String,

    @field:NotBlank
    val endTime: String
)
