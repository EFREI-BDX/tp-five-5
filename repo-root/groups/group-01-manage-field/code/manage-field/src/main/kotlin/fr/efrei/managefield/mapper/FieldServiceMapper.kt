package fr.efrei.managefield.mapper

import fr.efrei.managefield.entity.FieldEntity
import fr.efrei.managefield.service.dto.FieldViewResultDto
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

    fun toFieldViews(entities: List<FieldEntity>): List<FieldViewResultDto>
}
