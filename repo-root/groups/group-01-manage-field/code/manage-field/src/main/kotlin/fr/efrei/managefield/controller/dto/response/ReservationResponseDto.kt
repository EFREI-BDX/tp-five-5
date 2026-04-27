package fr.efrei.managefield.controller.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HTTP payload returned for a reservation.
 */
data class ReservationResponseDto(
    val id: String,

    @JsonProperty("field_id")
    val fieldId: String,

    @JsonProperty("status_id")
    val statusId: String,

    val date: String,

    @JsonProperty("start_time")
    val startTime: String,

    @JsonProperty("end_time")
    val endTime: String
)
