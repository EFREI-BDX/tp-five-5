package fr.efrei.managefield.mapper

import fr.efrei.managefield.entity.ActiveFieldEntity
import fr.efrei.managefield.entity.FieldEntity
import fr.efrei.managefield.service.dto.response.FieldDetailsViewResultDto
import fr.efrei.managefield.service.dto.response.FieldStatusViewResultDto
import fr.efrei.managefield.service.dto.response.FieldViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants

/**
 * Maps field persistence projections to service read DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface FieldServiceMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "statusId", source = "statusId")
    fun toFieldView(entity: FieldEntity): FieldViewResultDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "statusId", source = "statusId")
    fun toFieldView(entity: ActiveFieldEntity): FieldViewResultDto

    fun toActiveFieldViews(entities: List<ActiveFieldEntity>): List<FieldViewResultDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "name", source = "entity.name")
    @Mapping(target = "statusId", source = "entity.statusId")
    @Mapping(target = "status", source = "entity")
    @Mapping(target = "reservations", source = "reservations")
    fun toFieldDetails(
        entity: FieldEntity,
        reservations: List<ReservationViewResultDto>
    ): FieldDetailsViewResultDto

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "statusId")
    @Mapping(target = "code", source = "statusCode")
    @Mapping(target = "label", source = "statusLabel")
    fun toFieldStatusView(entity: FieldEntity): FieldStatusViewResultDto
}
