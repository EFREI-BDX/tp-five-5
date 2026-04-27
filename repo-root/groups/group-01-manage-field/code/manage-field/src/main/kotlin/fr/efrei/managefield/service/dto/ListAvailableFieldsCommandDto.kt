package fr.efrei.managefield.service.dto

import jakarta.validation.constraints.NotBlank

/**
 * Command used by the service layer to list fields available for a slot.
 */
data class ListAvailableFieldsCommandDto(
    @field:NotBlank
    val date: String,

    @field:NotBlank
    val startTime: String,

    @field:NotBlank
    val endTime: String
)
