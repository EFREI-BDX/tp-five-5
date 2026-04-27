package fr.efrei.managefield.service.dto.request

import jakarta.validation.constraints.NotBlank

/**
 * Command used by the service layer to change a field status.
 */
data class ChangeFieldStatusCommandDto(
    @field:NotBlank
    val fieldId: String,

    @field:NotBlank
    val statusId: String
)
