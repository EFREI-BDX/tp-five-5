package fr.efrei.managefield.service.dto

/**
 * Read model returned by the service layer for a field.
 */
data class FieldViewResultDto(
    val id: String,
    val name: String,
    val statusId: String
)
