package fr.efrei.managefield.mapper

import fr.efrei.managefield.controller.dto.request.CreateReservationRequestDto
import fr.efrei.managefield.controller.dto.request.UpdateReservationStatusRequestDto
import fr.efrei.managefield.controller.dto.response.ReservationResponseDto
import fr.efrei.managefield.controller.dto.response.ReservationStatusResponseDto
import fr.efrei.managefield.service.dto.request.ChangeReservationStatusCommandDto
import fr.efrei.managefield.service.dto.request.CreateReservationCommandDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps reservation HTTP DTOs to service commands and service DTOs to HTTP DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ReservationApiMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "statusId", source = "request.statusId")
    @Mapping(target = "date", source = "request.date")
    @Mapping(target = "startTime", source = "request.startTime")
    @Mapping(target = "endTime", source = "request.endTime")
    fun toCreateCommand(fieldId: String, request: CreateReservationRequestDto): CreateReservationCommandDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "reservationId", source = "reservationId")
    @Mapping(target = "statusId", source = "request.statusId")
    fun toChangeStatusCommand(
        fieldId: String,
        reservationId: String,
        request: UpdateReservationStatusRequestDto
    ): ChangeReservationStatusCommandDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "statusId", source = "statusId")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    fun toReservationResponse(result: ReservationViewResultDto): ReservationResponseDto

    fun toReservationResponses(results: List<ReservationViewResultDto>): List<ReservationResponseDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toReservationStatusResponse(result: ReservationStatusViewResultDto): ReservationStatusResponseDto
}
