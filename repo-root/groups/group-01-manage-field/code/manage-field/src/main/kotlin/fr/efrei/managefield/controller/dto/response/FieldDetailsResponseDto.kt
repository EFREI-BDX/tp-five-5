package fr.efrei.managefield.controller.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HTTP payload returned for a field detail lookup.
 */
data class FieldDetailsResponseDto(
    val id: String,
    val name: String,

    @JsonProperty("status_id")
    val statusId: String,

    val status: FieldStatusResponseDto,
    val reservations: List<ReservationResponseDto>
)
