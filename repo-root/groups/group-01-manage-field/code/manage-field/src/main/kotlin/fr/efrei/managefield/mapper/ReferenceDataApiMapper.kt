package fr.efrei.managefield.mapper

import fr.efrei.managefield.controller.dto.response.FieldStatusResponseDto
import fr.efrei.managefield.controller.dto.response.ReservationStatusResponseDto
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps reference data service DTOs to HTTP DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ReferenceDataApiMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toFieldStatusResponse(result: FieldStatusViewResultDto): FieldStatusResponseDto

    fun toFieldStatusResponses(results: List<FieldStatusViewResultDto>): List<FieldStatusResponseDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toReservationStatusResponse(result: ReservationStatusViewResultDto): ReservationStatusResponseDto

    fun toReservationStatusResponses(results: List<ReservationStatusViewResultDto>): List<ReservationStatusResponseDto>
}
