package fr.efrei.managefield.controller.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

/**
 * HTTP payload accepted when changing a reservation status.
 */
data class UpdateReservationStatusRequestDto(
    @field:NotBlank
    @JsonProperty("status_id")
    val statusId: String
)
