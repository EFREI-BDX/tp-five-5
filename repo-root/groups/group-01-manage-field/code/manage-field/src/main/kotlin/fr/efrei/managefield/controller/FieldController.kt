package fr.efrei.managefield.controller

import fr.efrei.managefield.controller.dto.request.UpdateFieldStatusRequestDto
import fr.efrei.managefield.controller.dto.response.FieldDetailsResponseDto
import fr.efrei.managefield.controller.dto.response.FieldResponseDto
import fr.efrei.managefield.mapper.FieldApiMapper
import fr.efrei.managefield.service.FieldService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Exposes HTTP endpoints for field lookup, availability and status changes.
 */
@Validated
@RestController
@RequestMapping("/v1/fields")
class FieldController(
    private val fieldService: FieldService,
    private val fieldApiMapper: FieldApiMapper
) {
    @GetMapping("/available")
    fun listAvailableFields(
        @RequestParam("date") @NotBlank date: String,
        @RequestParam("start_time") @NotBlank startTime: String,
        @RequestParam("end_time") @NotBlank endTime: String
    ): List<FieldResponseDto> {
        val command = fieldApiMapper.toListAvailableFieldsCommand(date, startTime, endTime)
        return fieldApiMapper.toFieldResponses(fieldService.listAvailableFields(command))
    }

    @GetMapping("/{field_id}")
    fun findById(@PathVariable("field_id") @NotBlank fieldId: String): FieldDetailsResponseDto {
        return fieldApiMapper.toFieldDetailsResponse(fieldService.findById(fieldId))
    }

    @PatchMapping("/{field_id}/status")
    fun changeStatus(
        @PathVariable("field_id") @NotBlank fieldId: String,
        @Valid @RequestBody request: UpdateFieldStatusRequestDto
    ): FieldResponseDto {
        val command = fieldApiMapper.toChangeStatusCommand(fieldId, request)
        return fieldApiMapper.toFieldResponse(fieldService.changeStatus(command))
    }
}
