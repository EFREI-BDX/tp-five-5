package fr.efrei.managefield.service.implementation

import fr.efrei.managefield.domain.valueobject.DomainId
import fr.efrei.managefield.domain.valueobject.TimeSlot
import fr.efrei.managefield.entity.ReservationEntity
import fr.efrei.managefield.mapper.ReservationServiceMapper
import fr.efrei.managefield.repository.FieldRepository
import fr.efrei.managefield.repository.ReservationRepository
import fr.efrei.managefield.service.ReservationService
import fr.efrei.managefield.service.dto.request.ChangeReservationStatusCommandDto
import fr.efrei.managefield.service.dto.request.CreateReservationCommandDto
import fr.efrei.managefield.service.dto.response.ReservationViewResultDto
import fr.efrei.managefield.service.exception.ApplicationInternalException
import fr.efrei.managefield.service.exception.ApplicationNotFoundException
import fr.efrei.managefield.service.exception.ApplicationValidationException
import fr.efrei.managefield.service.requireSuccess
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.util.UUID

/**
 * Default [ReservationService] implementation.
 *
 * Reads go through JPA view projections; writes go through repository
 * `@Procedure` methods.
 */
@Service
@Validated
class ReservationServiceImpl(
    private val fieldRepository: FieldRepository,
    private val reservationRepository: ReservationRepository,
    private val reservationServiceMapper: ReservationServiceMapper
) : ReservationService {
    @Transactional(readOnly = true)
    override fun listByFieldId(fieldId: String): List<ReservationViewResultDto> {
        val id = parseId(fieldId, "field_id")
        if (!fieldRepository.existsById(id.value)) {
            throw ApplicationNotFoundException("field not found")
        }

        return reservationServiceMapper.toReservationViews(
            reservationRepository.findAllByFieldIdOrderByDateAscStartTimeAsc(id.value)
        )
    }

    @Transactional
    override fun create(command: CreateReservationCommandDto): ReservationViewResultDto {
        val fieldId = parseId(command.fieldId, "field_id")
        val statusId = parseId(command.statusId, "status_id")
        val slot = parseSlot(command.date, command.startTime, command.endTime)
        val reservationId = UUID.randomUUID().toString()
        val response = reservationRepository
            .createReservation(
                reservationId = reservationId,
                fieldId = fieldId.value,
                statusId = statusId.value,
                date = slot.date,
                startTime = slot.startTime,
                endTime = slot.endTime
            )
            .requireSuccess()

        val createdReservationId = response.getReservationId()
            ?: throw ApplicationInternalException("create reservation procedure did not return a reservation_id")

        return reservationServiceMapper.toReservationView(findReservationForFieldOrThrow(createdReservationId, fieldId.value))
    }

    @Transactional
    override fun changeStatus(command: ChangeReservationStatusCommandDto): ReservationViewResultDto {
        val fieldId = parseId(command.fieldId, "field_id")
        val reservationId = parseId(command.reservationId, "reservation_id")
        val statusId = parseId(command.statusId, "status_id")
        reservationRepository
            .changeReservationStatus(fieldId.value, reservationId.value, statusId.value)
            .requireSuccess()

        return reservationServiceMapper.toReservationView(findReservationForFieldOrThrow(reservationId.value, fieldId.value))
    }

    private fun findReservationForFieldOrThrow(reservationId: String, fieldId: String): ReservationEntity {
        val reservation = reservationRepository.findByIdOrNull(reservationId)
            ?: throw ApplicationNotFoundException("reservation not found")

        if (reservation.fieldId != fieldId) {
            throw ApplicationNotFoundException("reservation not found")
        }

        return reservation
    }

    private fun parseId(raw: String, field: String): DomainId {
        return try {
            DomainId.from(raw)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException("${field}: ${exception.message}")
        }
    }

    private fun parseSlot(date: String, startTime: String, endTime: String): TimeSlot {
        return try {
            TimeSlot.from(date, startTime, endTime)
        } catch (exception: IllegalArgumentException) {
            throw ApplicationValidationException(exception.message ?: "slot is invalid")
        }
    }
}
