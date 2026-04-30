package fr.efrei.managefield.mapper

import fr.efrei.managefield.domain.entity.Field
import fr.efrei.managefield.domain.enums.FieldStatusCode
import fr.efrei.managefield.service.dto.response.FieldDetailsViewResultDto
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.FieldViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps field domain aggregates to service read DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface FieldServiceMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "name", source = "name.value")
    @Mapping(target = "statusId", source = "status.id")
    fun toFieldView(entity: Field): FieldViewResultDto

    fun toFieldViews(entities: List<Field>): List<FieldViewResultDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "entity.id.value")
    @Mapping(target = "name", source = "entity.name.value")
    @Mapping(target = "statusId", source = "entity.status.id")
    @Mapping(target = "status", source = "entity.status")
    @Mapping(target = "reservations", source = "reservations")
    fun toFieldDetails(
        entity: Field,
        reservations: List<ReservationViewResultDto>
    ): FieldDetailsViewResultDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toFieldStatusView(status: FieldStatusCode): FieldStatusViewResultDto
}
