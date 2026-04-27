package fr.efrei.managefield.mapper

import fr.efrei.managefield.controller.dto.request.UpdateFieldStatusRequestDto
import fr.efrei.managefield.controller.dto.response.FieldDetailsResponseDto
import fr.efrei.managefield.controller.dto.response.FieldResponseDto
import fr.efrei.managefield.controller.dto.response.FieldStatusResponseDto
import fr.efrei.managefield.controller.dto.response.ReservationResponseDto
import fr.efrei.managefield.controller.dto.response.ReservationStatusResponseDto
import fr.efrei.managefield.service.dto.request.ChangeFieldStatusCommandDto
import fr.efrei.managefield.service.dto.response.FieldDetailsViewResultDto
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.FieldViewResultDto
import fr.efrei.managefield.service.dto.request.ListAvailableFieldsCommandDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps field HTTP DTOs to service commands and service DTOs to HTTP DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface FieldApiMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "date", source = "date")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    fun toListAvailableFieldsCommand(date: String, startTime: String, endTime: String): ListAvailableFieldsCommandDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "statusId", source = "request.statusId")
    fun toChangeStatusCommand(fieldId: String, request: UpdateFieldStatusRequestDto): ChangeFieldStatusCommandDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "statusId", source = "statusId")
    fun toFieldResponse(result: FieldViewResultDto): FieldResponseDto

    fun toFieldResponses(results: List<FieldViewResultDto>): List<FieldResponseDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "statusId", source = "statusId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "reservations", source = "reservations")
    fun toFieldDetailsResponse(result: FieldDetailsViewResultDto): FieldDetailsResponseDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toFieldStatusResponse(result: FieldStatusViewResultDto): FieldStatusResponseDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "statusId", source = "statusId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    fun toReservationResponse(result: ReservationViewResultDto): ReservationResponseDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toReservationStatusResponse(result: ReservationStatusViewResultDto): ReservationStatusResponseDto
}
