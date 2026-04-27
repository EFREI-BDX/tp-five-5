package fr.efrei.managefield.mapper

import fr.efrei.managefield.controller.dto.request.UpdateFieldStatusRequestDto
import fr.efrei.managefield.controller.dto.response.FieldResponseDto
import fr.efrei.managefield.service.dto.ChangeFieldStatusCommandDto
import fr.efrei.managefield.service.dto.FieldViewResultDto
import fr.efrei.managefield.service.dto.ListAvailableFieldsCommandDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps field HTTP DTOs to service commands and service DTOs to HTTP DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface FieldApiMapper {
    fun toListAvailableFieldsCommand(date: String, startTime: String, endTime: String): ListAvailableFieldsCommandDto {
        return ListAvailableFieldsCommandDto(date = date, startTime = startTime, endTime = endTime)
    }

    fun toChangeStatusCommand(fieldId: String, request: UpdateFieldStatusRequestDto): ChangeFieldStatusCommandDto {
        return ChangeFieldStatusCommandDto(fieldId = fieldId, statusId = request.statusId)
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "statusId", source = "statusId")
    fun toFieldResponse(result: FieldViewResultDto): FieldResponseDto

    fun toFieldResponses(results: List<FieldViewResultDto>): List<FieldResponseDto>
}
