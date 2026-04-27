package fr.efrei.managefield.controller.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

/**
 * HTTP payload accepted when creating a reservation.
 */
data class CreateReservationRequestDto(
    @field:NotBlank
    @JsonProperty("status_id")
    val statusId: String,

    @field:NotBlank
    val date: String,

    @field:NotBlank
    @JsonProperty("start_time")
    val startTime: String,

    @field:NotBlank
    @JsonProperty("end_time")
    val endTime: String
)
