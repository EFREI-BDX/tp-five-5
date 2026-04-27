package fr.efrei.managefield.controller.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * HTTP payload returned when an application error is raised.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ErrorResponseDto(
    val error: String,
    val message: String,
    val details: List<ValidationErrorDetailResponseDto> = emptyList()
)

/**
 * Field-level validation detail returned for malformed HTTP input.
 */
data class ValidationErrorDetailResponseDto(
    val field: String,
    val issue: String,
    val message: String
)
