package fr.efrei.managefield.controller.dto.response

/**
 * HTTP payload returned for a field status.
 */
data class FieldStatusResponseDto(
    val id: String,
    val code: String,
    val label: String
)
