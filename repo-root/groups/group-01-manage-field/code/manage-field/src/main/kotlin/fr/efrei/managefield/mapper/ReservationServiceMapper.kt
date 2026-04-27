package fr.efrei.managefield.mapper

import fr.efrei.managefield.entity.ReservationEntity
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Maps reservation persistence projections to service read DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ReservationServiceMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fieldId", source = "fieldId")
    @Mapping(target = "statusId", source = "statusId")
    @Mapping(target = "status", source = "entity")
    @Mapping(target = "date", expression = "java(formatDate(entity.getDate()))")
    @Mapping(target = "startTime", expression = "java(formatTime(entity.getStartTime()))")
    @Mapping(target = "endTime", expression = "java(formatTime(entity.getEndTime()))")
    fun toReservationView(entity: ReservationEntity): ReservationViewResultDto

    fun toReservationViews(entities: List<ReservationEntity>): List<ReservationViewResultDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "statusId")
    @Mapping(target = "code", source = "statusCode")
    @Mapping(target = "label", source = "statusLabel")
    fun toReservationStatusView(entity: ReservationEntity): ReservationStatusViewResultDto

    fun formatDate(date: LocalDate?): String {
        return requireNotNull(date) { "reservation date must not be null" }.toString()
    }

    fun formatTime(time: LocalTime?): String {
        return requireNotNull(time) { "reservation time must not be null" }.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}
