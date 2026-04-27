package fr.efrei.managefield.service.dto

/**
 * Read model returned by the service layer for a field status.
 */
data class FieldStatusViewResultDto(
    val id: String,
    val code: String,
    val label: String
)
