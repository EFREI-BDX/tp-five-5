package fr.efrei.managefield.mapper

import fr.efrei.managefield.entity.FieldStatusEntity
import fr.efrei.managefield.entity.ReservationStatusEntity
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps reference data persistence projections to service read DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ReferenceDataServiceMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toFieldStatusView(entity: FieldStatusEntity): FieldStatusViewResultDto

    fun toFieldStatusViews(entities: List<FieldStatusEntity>): List<FieldStatusViewResultDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toReservationStatusView(entity: ReservationStatusEntity): ReservationStatusViewResultDto

    fun toReservationStatusViews(entities: List<ReservationStatusEntity>): List<ReservationStatusViewResultDto>
}
