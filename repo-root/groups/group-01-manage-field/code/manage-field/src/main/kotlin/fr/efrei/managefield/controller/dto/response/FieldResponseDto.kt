package fr.efrei.managefield.controller.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * HTTP payload returned for a field.
 */
data class FieldResponseDto(
    val id: String,
    val name: String,

    @JsonProperty("status_id")
    val statusId: String
)
