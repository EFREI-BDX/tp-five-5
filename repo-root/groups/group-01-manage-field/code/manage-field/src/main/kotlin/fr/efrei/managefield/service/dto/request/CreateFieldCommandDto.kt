package fr.efrei.managefield.service.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * Internal command used to create a field through the stored procedure.
 */
data class CreateFieldCommandDto(
    val fieldId: String? = null,

    @field:NotBlank
    @field:Size(max = 100)
    val name: String,

    @field:NotBlank
    val statusId: String
)
