package fr.efrei.managefield.mapper

import fr.efrei.managefield.domain.entity.Reservation
import fr.efrei.managefield.domain.enums.ReservationStatusCode
import fr.efrei.managefield.service.dto.response.ReservationStatusViewResultDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import java.time.format.DateTimeFormatter

/**
 * Maps reservation domain aggregates to service read DTOs.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ReservationServiceMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "fieldId", source = "fieldId.value")
    @Mapping(target = "statusId", source = "status.id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "date", source = "slot.date")
    @Mapping(target = "startTime", expression = "java(formatTime(entity.getSlot().getStartTime()))")
    @Mapping(target = "endTime", expression = "java(formatTime(entity.getSlot().getEndTime()))")
    fun toReservationView(entity: Reservation): ReservationViewResultDto

    fun toReservationViews(entities: List<Reservation>): List<ReservationViewResultDto>

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "label", source = "label")
    fun toReservationStatusView(status: ReservationStatusCode): ReservationStatusViewResultDto

    fun formatTime(time: java.time.LocalTime): String {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}
