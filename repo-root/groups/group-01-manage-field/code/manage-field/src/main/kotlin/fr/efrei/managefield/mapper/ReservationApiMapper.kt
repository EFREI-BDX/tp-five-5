package fr.efrei.managefield.mapper

import fr.efrei.managefield.controller.dto.request.CreateReservationRequestDto
import fr.efrei.managefield.controller.dto.request.UpdateReservationStatusRequestDto
import fr.efrei.managefield.controller.dto.response.ReservationResponseDto
import fr.efrei.managefield.service.dto.ChangeReservationStatusCommandDto
import fr.efrei.managefield.service.dto.CreateReservationCommandDto
import fr.efrei.managefield.service.dto.ReservationViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps reservation HTTP DTOs to service commands and service DTOs to HTTP DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ReservationApiMapper {
    fun toCreateCommand(fieldId: String, request: CreateReservationRequestDto): CreateReservationCommandDto {
        return CreateReservationCommandDto(
            fieldId = fieldId,
            statusId = request.statusId,
            date = request.date,
            startTime = request.startTime,
            endTime = request.endTime
        )
    }

    fun toChangeStatusCommand(
        fieldId: String,
        reservationId: String,
        request: UpdateReservationStatusRequestDto
    ): ChangeReservationStatusCommandDto {
        return ChangeReservationStatusCommandDto(
            fieldId = fieldId,
            reservationId = reservationId,
            statusId = request.statusId
        )
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "statusId", source = "statusId")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    fun toReservationResponse(result: ReservationViewResultDto): ReservationResponseDto
}
