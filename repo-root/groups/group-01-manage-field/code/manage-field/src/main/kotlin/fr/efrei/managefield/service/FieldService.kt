package fr.efrei.managefield.service

import fr.efrei.managefield.service.dto.ChangeFieldStatusCommandDto
import fr.efrei.managefield.service.dto.CreateFieldCommandDto
import fr.efrei.managefield.service.dto.FieldViewResultDto
import fr.efrei.managefield.service.dto.ListAvailableFieldsCommandDto
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

/**
 * Defines field operations exposed by the application layer.
 */
interface FieldService {
    /**
     * Lists active fields available for a requested slot.
     */
    fun listAvailableFields(@Valid @NotNull command: ListAvailableFieldsCommandDto): List<FieldViewResultDto>

    /**
     * Returns a field by identifier.
     */
    fun findById(@NotBlank fieldId: String): FieldViewResultDto

    /**
     * Internal operation creating a field through the stored procedure.
     */
    fun create(@Valid @NotNull command: CreateFieldCommandDto): FieldViewResultDto

    /**
     * Changes the status of a field through the stored procedure.
     */
    fun changeStatus(@Valid @NotNull command: ChangeFieldStatusCommandDto): FieldViewResultDto
}
