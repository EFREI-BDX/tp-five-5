package fr.efrei.managefield.controller.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

/**
 * HTTP payload accepted when changing a field status.
 */
data class UpdateFieldStatusRequestDto(
    @field:NotBlank
    @JsonProperty("status_id")
    val statusId: String
)
